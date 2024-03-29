package semillero.ecosistema.service.implmentations;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.errors.InvalidRequestException;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import semillero.ecosistema.dto.*;
import semillero.ecosistema.entity.*;
import semillero.ecosistema.enums.ProviderEnum;
import semillero.ecosistema.exception.*;
import semillero.ecosistema.mapper.ProviderMapper;
import semillero.ecosistema.repository.*;
import semillero.ecosistema.service.CloudinaryService;
import semillero.ecosistema.service.contracts.ImageService;
import semillero.ecosistema.service.contracts.ProviderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProviderMapper providerMapper;
    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;
    private final CloudinaryService cloudinaryService;
    private final GeoApiContext geoApiContext;
    private final ImageService imageService;

    private static final String STATUS_INITIAL = ProviderEnum.REVISION_INICIAL.name();
    private static final String CAMBIOS_REALIZADOS = ProviderEnum.CAMBIOS_REALIZADOS.name();

    @Override
    public List<ProviderResponseDto> getAll() {
        List<ProviderEntity> providerEntityList = providerRepository.findAll();
        List<ProviderResponseDto> providerResponseDtoList = providerMapper.toDtoList(providerEntityList);
        mapperParamsProvider(providerEntityList, providerResponseDtoList);
        return providerResponseDtoList;
    }

    @Override
    public List<ProviderResponseDto> getAccepted() {
        List<ProviderEntity> providerEntityList = providerRepository.listarAceptados();
        List<ProviderResponseDto> providerResponseDtoList = providerMapper.toDtoList(providerEntityList);
        mapperParamsProvider(providerEntityList, providerResponseDtoList);
        return providerResponseDtoList;
    }

    @Override
    public List<ProviderResponseDto> getByName(String name) {
        List<ProviderEntity> providerEntityList = providerRepository.searchProviderByName(name);
        List<ProviderResponseDto> providerResponseDtoList = providerMapper.toDtoList(providerEntityList);

        mapperParamsProvider(providerEntityList, providerResponseDtoList);
        return providerResponseDtoList;
    }

    @Override
    public List<ProviderResponseDto> getByCategory(String categoria) {
        List<ProviderEntity> providerEntityList = providerRepository.listarPorCategoria(categoria);
        List<ProviderResponseDto> providerResponseDtoList = providerMapper.toDtoList(providerEntityList);

        mapperParamsProvider(providerEntityList, providerResponseDtoList);
        return providerResponseDtoList;
    }

    @Override
    public List<ProviderResponseDto> getByLocation(double latitude, double longitude) {
        try {
            LatLng latLng = new LatLng(latitude, longitude);
            GeocodingResult[] results = GeocodingApi.reverseGeocode(geoApiContext, latLng).await();

            String province = null;
            String city = null;
            String country = null;

            if (results != null && results.length > 0) {
                GeocodingResult result = results[0];

                for (AddressComponent ac : result.addressComponents) {
                    for (AddressComponentType acType : ac.types) {
                        System.out.println(acType);
                        if (acType == AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1) {
                            province = ac.shortName;
                        } else if (acType == AddressComponentType.LOCALITY) {
                            city = ac.shortName;
                        } else if (acType == AddressComponentType.COUNTRY) {
                            country = ac.longName;
                        }
                    }

                }

                List<ProviderEntity> providerEntityList = providerRepository.findByLocation(country, province, city);
                List<ProviderResponseDto> providerResponseDtoList = providerMapper.toDtoList(providerEntityList);

                mapperParamsProvider(providerEntityList, providerResponseDtoList);
                return providerResponseDtoList;

            }
        } catch (InvalidRequestException e) {
            System.err.println("Error de solicitud no válida: " + e.getMessage());
            throw new RuntimeException(e.getMessage());

        } catch (ApiException | InterruptedException | IOException e) {
            System.err.println("Otro tipo de error: " + e.getMessage());
        }

        return null;
    }


    private void mapperParamsProvider(List<ProviderEntity> providerEntityList, List<ProviderResponseDto> providerResponseDtoList) {
        providerResponseDtoList.stream().forEach(providerResponseDto -> {
            providerEntityList.stream().filter(providerEntity -> providerEntity.getId() == providerResponseDto.getId())
                    .forEach(provider -> {
                        String categoryName = provider.getCategory().getNombre();
                        String countryName = provider.getCountry().getName();
                        String provinceName = provider.getProvince().getName();

                        providerResponseDto.setCategoryName(categoryName);
                        providerResponseDto.setCountryName(countryName);
                        providerResponseDto.setProvinceName(provinceName);
                    });
        });
    }


    @Override
    public ProviderEntity save(String userId, ProviderRequestDto providerRequestDto) throws IOException {
        if (providerRequestDto.getFacebook() == null || providerRequestDto.getFacebook().equals("") || providerRequestDto.getFacebook().equals("undefined")){
            providerRequestDto.setFacebook("https://www.facebook.com/");
        }
        if (providerRequestDto.getInstagram() == null || providerRequestDto.getInstagram().equals("") || providerRequestDto.getInstagram().equals("undefined")){
            providerRequestDto.setInstagram("https://www.instagram.com/");
        }

        UserEntity userEntity = getUsersById(userId);
        validateMaxProviders(userEntity);

        ProviderMapper converter = Mappers.getMapper(ProviderMapper.class);
        ProviderEntity providerEntity = converter.toEntity(providerRequestDto);
        parametersInitialProvider(providerEntity, userEntity);

        ProvinceEntity provincia = provinceRepository.getReferenceById(providerRequestDto.getProvinceId());
        CountryEntity pais = countryRepository.getReferenceById(providerRequestDto.getCountryId());
        CategoryEntity categoria = categoryRepository.getReferenceById(providerRequestDto.getCategoryId());

        providerEntity.setProvince(provincia);
        providerEntity.setCountry(pais);
        providerEntity.setCategory(categoria);
        List<ImageEntity> images = agregarImagenAProveedor(providerRequestDto.getImages());
        providerEntity.setImages (images);

        ProviderEntity providerSaved = providerRepository.save(providerEntity);
        return providerSaved;
    }

    //Tener en cuenta
    public List<ImageEntity> agregarImagenAProveedor (List <MultipartFile> imagenes) throws IOException {

        List<ImageEntity> listaImagen = new ArrayList<>();
        try {
            for (MultipartFile imagen: imagenes) {

                // Subir la imagen a Cloudinary
                Map subirImagen = cloudinaryService.upload(imagen);

                // Crear y guardar la entidad de Imagen
                ImageEntity image = new ImageEntity();

                image.setName((String) subirImagen.get("original_filename"));
                image.setImagenUrl((String) subirImagen.get("url"));
                image.setCloudinaryId((String) subirImagen.get("public_id"));
//                     ImageEntity im = imageService.save(image);
                listaImagen.add(image);
            }
            return listaImagen;
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la imagen.");
        }
    }

    /**
     * Buscar usuario por su ID
     *
     * @param userId Id de usuario a buscar
     * @return UserEntity encontrado
     */

    private UserEntity getUsersById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistException());
    }

    /**
     * Validar el maximo de proveedores al crear por usuario
     */
    private void validateMaxProviders(UserEntity userEntity) {
        if (userEntity.getProviderEntityList().size() >= 3) {
            throw new ProviderMaxCreatedException();
        }
    }

    /**
     * Establecer parametros iniciales de proveedor
     */
    private void parametersInitialProvider(ProviderEntity providerEntity, UserEntity userEntity) {

        providerEntity.setStatus(STATUS_INITIAL);
        providerEntity.setFeedBack("Proveedor en revisión");
                providerEntity.setIsNew(true);
        providerEntity.setDeleted(false);
        providerEntity.setOpenFullImage(false);
        providerEntity.setUser(userEntity);
    }

    @Override
    public List<ProviderResponseDto> getByStatus() {
        List<ProviderEntity> providerEntityList = providerRepository.findAllByStatus(STATUS_INITIAL, CAMBIOS_REALIZADOS);
        List<ProviderResponseDto> providerResponseDtoList = providerMapper.toDtoList(providerEntityList);
        mapperParamsProvider(providerEntityList, providerResponseDtoList);
        return providerResponseDtoList;
    }

    @Override
    public Boolean updateStatus(ProviderUpdateStatusRequestDto providerUpdateStatusRequestDto) {
        ProviderEntity providerEntity = providerRepository.findById(providerUpdateStatusRequestDto.getProviderId())
                .orElseThrow(() -> new ProviderNotExistException());

        if (providerEntity != null) {
            providerEntity.setStatus(providerUpdateStatusRequestDto.getNewStatus());
            providerEntity.setFeedBack(providerUpdateStatusRequestDto.getNewFeedBack());
            providerRepository.save(providerEntity);
            return true;
        }

        return false;
    }

    @Override
    public ResponseEntity<?> update(ProviderUpdateRequestDto providerUpdateRequestDto){
        if (providerUpdateRequestDto.isImagenesBorrarVacio()){
            providerUpdateRequestDto.setImagenesParaBorrar(new ArrayList<>());
        }
        if (providerUpdateRequestDto.isImagenesNuevasVacio()){
            providerUpdateRequestDto.setImagenesNuevas(new ArrayList<>());
        }
        if (providerUpdateRequestDto.getFacebook() == null || providerUpdateRequestDto.getFacebook().equals("") || providerUpdateRequestDto.getFacebook().equals("undefined")){
            providerUpdateRequestDto.setFacebook("https://www.facebook.com/");
        }
        if (providerUpdateRequestDto.getInstagram() == null || providerUpdateRequestDto.getInstagram().equals("") || providerUpdateRequestDto.getFacebook().equals("undefined")){
            providerUpdateRequestDto.setInstagram("https://www.instagram.com/");
        }



        try {
            //ProviderEntity providerEntity = providerMapper.toEntityUpdate(providerUpdateRequestDto); //Entiendo que esto me da un provider Entity
        /*List<ImageEntity> images = agregarImagenAProveedor(providerUpdateRequestDto.getImages());
        existProvider.getImages().clear();
//        providerEntity.getImagenes().addAll(images);
        providerEntity.setImages(images);*/
            ProviderEntity providerEntity = providerRepository.findById(providerUpdateRequestDto.getId())
                    .orElseThrow(ProviderNotExistException::new);
            providerEntity.setName(providerUpdateRequestDto.getName());
            providerEntity.setDescription(providerUpdateRequestDto.getDescription());
            providerEntity.setNumberPhone(providerUpdateRequestDto.getNumberPhone());
            providerEntity.setEmail(providerUpdateRequestDto.getEmail());
            providerEntity.setFacebook(providerUpdateRequestDto.getFacebook());
            providerEntity.setInstagram(providerUpdateRequestDto.getInstagram());
            providerEntity.setAbout(providerUpdateRequestDto.getAbout());
            providerEntity.setStatus(ProviderEnum.CAMBIOS_REALIZADOS.name());
            providerEntity.setFeedBack("Los cambios han sido realizados. El administrador realizará la revisión y devolución correspondiente");

            ProvinceEntity provincia = provinceRepository.getReferenceById(providerUpdateRequestDto.getProvinceId());
            CountryEntity pais = countryRepository.getReferenceById(providerUpdateRequestDto.getCountryId());
            CategoryEntity categoria = categoryRepository.getReferenceById(providerUpdateRequestDto.getCategoryId());

            providerEntity.setProvince(provincia);
            providerEntity.setCountry(pais);
            providerEntity.setCategory(categoria);
            providerEntity.setCity(providerUpdateRequestDto.getCity());

            List<ImageEntity> imagenes = modificarImagenEnProveedor(providerUpdateRequestDto, providerEntity);
            providerEntity.getImages().addAll(imagenes);

            providerRepository.save(providerEntity);
            return ResponseEntity.ok().body("UPDATED");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }


    }

    //Metodos para modificar Imagenes
    @Transactional
    public List<ImageEntity> modificarImagenEnProveedor
    (ProviderUpdateRequestDto providerUpdateRequestDto, ProviderEntity provider) throws IOException {
        List<String> imagenesParaBorrar = new ArrayList<>(providerUpdateRequestDto.getImagenesParaBorrar());
        for (String id : imagenesParaBorrar) {
            ImageEntity imagen = imageService.getImagen(id).get();
            provider.getImages().remove(imagen);
            cloudinaryService.delete(imagen.getCloudinaryId());
            imageService.delete(id);
        }
        List<ImageEntity> listaImagen = new ArrayList();
        if (providerUpdateRequestDto.getImagenesNuevas().size()>0) {
            listaImagen = agregarImagenAProveedor(providerUpdateRequestDto.getImagenesNuevas());
        }
        return listaImagen;
    }
    /**
     * METODO PARA VALIDAR
     *
     * @param value        Valor a comparar si es NULL
     * @param defaultValue Valor que devolvera si "value" es NULL
     * @return "value" si es NULL o "defaultValue" si "value" no es NULL
     */
    private <T> T defaultIfNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    private CategoryEntity getCategoryById(String categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotExistException());
    }

    private CountryEntity getCountryById(String countryId) {
        return countryRepository.findById(countryId).orElseThrow(() -> new CountryNotExistException());
    }

    private ProvinceEntity getProvinceById(String provinceId) {
        return provinceRepository.findById(provinceId).orElseThrow(() -> new ProvinceNotExistException());
    }

    private ProviderEntity getProviderById(String providerId) {
        return providerRepository.findById(providerId).orElse(null);
    }

    @Override
    public List<ProviderResponseDto> getByUser(String username) {
        List<ProviderEntity> providers = providerRepository.listarPorUsuario(username);
        List<ProviderResponseDto> providerResponseDtoList = providerMapper.toDtoList(providers);
        mapperParamsProvider(providers, providerResponseDtoList);
        return providerResponseDtoList;
    }


}