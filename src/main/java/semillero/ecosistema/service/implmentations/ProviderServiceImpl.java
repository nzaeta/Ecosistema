package semillero.ecosistema.service.implmentations;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import semillero.ecosistema.Dto.*;
import semillero.ecosistema.entity.*;
import semillero.ecosistema.enums.ProviderEnum;
import semillero.ecosistema.exception.*;
import semillero.ecosistema.mapper.ProviderMapper;
import semillero.ecosistema.repository.*;
import semillero.ecosistema.service.CloudinaryService;
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
        providerEntity.setImagenes (images);

        ProviderEntity providerSaved = providerRepository.save(providerEntity);
        return providerSaved;
    }

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
    private UserEntity getUsersById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistException());
    }

    /**
     * Validar el maximo de proveedores al crear por usuario
     */
    private void validateMaxProviders(UserEntity userEntity) {
        if(userEntity.getProviderEntityList().size() > 3) {
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

        if(providerEntity != null) {
            providerEntity.setStatus(providerUpdateStatusRequestDto.getNewStatus());
            providerEntity.setFeedBack(providerUpdateStatusRequestDto.getNewFeedBack());
            providerRepository.save(providerEntity);
            return true;
        }

        return false;
    }

    @Override
    public ProviderEntity update(ProviderUpdateRequestDto providerUpdateRequestDto) throws IOException {
        UserEntity userEntity = getUsersById(providerUpdateRequestDto.getUsersId());
        CategoryEntity categoryEntity = getCategoryById(providerUpdateRequestDto.getCategoryId());
        CountryEntity countryEntity = getCountryById(providerUpdateRequestDto.getCountryId());
        ProvinceEntity provinceEntity = getProvinceById(providerUpdateRequestDto.getProvinceId());

        ProviderEntity existProvider = getProviderById(providerUpdateRequestDto.getId());

        if(existProvider == null) {
            throw new ProviderNotExistException();
        }

        /**
         *  ESTABLECE VALORES PREDETERMINADOS DE LA BASE DE DATOS SI EN EL ProviderUpdateRequestDto NO SE PASA ALGUN VALOR.
         *  CON EL PROPOSITO PARA QUE NO GUARDE NULL AL MOMENTO DE ACTUALIZAR UN PROVEEDOR
         */
        providerUpdateRequestDto.setIsNew(defaultIfNull(providerUpdateRequestDto.getIsNew(), existProvider.getIsNew()));
        providerUpdateRequestDto.setDeleted(defaultIfNull(providerUpdateRequestDto.getDeleted(), existProvider.getDeleted()));
        providerUpdateRequestDto.setOpenFullImage(defaultIfNull(providerUpdateRequestDto.getOpenFullImage(), existProvider.getOpenFullImage()));
        providerUpdateRequestDto.setStatus(defaultIfNull(providerUpdateRequestDto.getStatus(), existProvider.getStatus()));

        /************/


        ProviderEntity providerEntity = providerMapper.toEntityUpdate(providerUpdateRequestDto);
        List<ImageEntity> images = agregarImagenAProveedor(providerUpdateRequestDto.getImages());
        providerEntity.getImagenes().clear();
        providerEntity.getImagenes().addAll(images);
        providerEntity.setUser(userEntity);
        providerEntity.setCategory(categoryEntity);
        providerEntity.setCountry(countryEntity);
        providerEntity.setProvince(provinceEntity);

        return providerRepository.save(providerEntity);
    }

    /**
     * METODO PARA VALIDAR
     * @param value Valor a comparar si es NULL
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



}