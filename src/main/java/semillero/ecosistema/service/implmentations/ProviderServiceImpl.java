package semillero.ecosistema.service.implmentations;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import semillero.ecosistema.Dto.ProviderRequestDto;
import semillero.ecosistema.Dto.ProviderResponseDto;
import semillero.ecosistema.Dto.ProviderUpdateRequestDto;
import semillero.ecosistema.Dto.ProviderUpdateStatusRequestDto;
import semillero.ecosistema.entity.*;
import semillero.ecosistema.enums.ProviderEnum;
import semillero.ecosistema.exception.*;
import semillero.ecosistema.mapper.ProviderMapper;
import semillero.ecosistema.repository.*;
import semillero.ecosistema.service.contracts.ProviderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProviderMapper providerMapper;
    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;
    private final GeoApiContext geoApiContext;

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
        } catch (Exception e) {
            e.printStackTrace();
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
    public ProviderEntity save(String userId, ProviderRequestDto providerRequestDto) {
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

        ProviderEntity providerSaved = providerRepository.save(providerEntity);
        return providerSaved;
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
    public ProviderEntity update(ProviderUpdateRequestDto providerUpdateRequestDto) {
        UserEntity userEntity = getUsersById(providerUpdateRequestDto.getUsersId());
        CategoryEntity categoryEntity = getCategoryById(providerUpdateRequestDto.getCategoryId());
        CountryEntity countryEntity = getCountryById(providerUpdateRequestDto.getCountryId());
        ProvinceEntity provinceEntity = getProvinceById(providerUpdateRequestDto.getProvinceId());

        ProviderEntity existProvider = getProviderById(providerUpdateRequestDto.getId());

        if (existProvider == null) {
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

        providerEntity.setUser(userEntity);
        providerEntity.setCategory(categoryEntity);
        providerEntity.setCountry(countryEntity);
        providerEntity.setProvince(provinceEntity);

        return providerRepository.save(providerEntity);
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


}