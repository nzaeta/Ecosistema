package semillero.ecosistema.service.implmentations;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import semillero.ecosistema.Dto.ProviderRequestDto;
import semillero.ecosistema.Dto.ProviderResponseDto;
import semillero.ecosistema.entity.*;
import semillero.ecosistema.enums.ProviderEnum;
import semillero.ecosistema.exception.ProviderMaxCreatedException;
import semillero.ecosistema.exception.ProviderNotExistException;
import semillero.ecosistema.exception.UserNotExistException;
import semillero.ecosistema.mapper.ProviderMapper;
import semillero.ecosistema.repository.*;
import semillero.ecosistema.service.contracts.ProviderService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProviderMapper providerMapper;
    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;

    private static final String STATUS_INITIAL = ProviderEnum.REVISION_INICIAL.name();

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
                        String countryName = provider.getCountry().getNombre();
                        String provinceName = provider.getProvince().getNombre();

                        providerResponseDto.setCategoryName(categoryName);
                        providerResponseDto.setCountryName(countryName);
                        providerResponseDto.setProvinceName(provinceName);
                    });
        });
    }


    @Override
    public ProviderEntity save(Long userId, ProviderRequestDto providerRequestDto) {
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
     * @param userId Id de usuario a buscar
     * @return UserEntity encontrado
     */
    private UserEntity getUsersById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistException());
    }

    /**
     * Validar el maximo de proveedores al crear por usuario
     */
    private void validateMaxProviders(UserEntity userEntity) {
        if(userEntity.getProviderEntityList().size() >= 3) {
            throw new ProviderMaxCreatedException();
        }
    }

    /**
     * Establecer parametros iniciales de proveedor
     */
    private void parametersInitialProvider(ProviderEntity providerEntity, UserEntity userEntity) {
        providerEntity.setStatus(STATUS_INITIAL);
        providerEntity.setIsNew(true);
        providerEntity.setActive(true);
        providerEntity.setDeleted(false);
        providerEntity.setOpenFullImage(false);
        providerEntity.setUser(userEntity);
    }

//    @Override
//    public ProviderEntity update(Long userId, ProviderEntity providerEntity) {
//        UserEntity userEntity = getUsersById(userId);
//        System.out.println("ESTOY");
//        Optional<ProviderEntity> existProvider = providerRepository.findById(providerEntity.getId());
//        System.out.println("ESTOY 2 " );
//        if(existProvider.isEmpty()) {
//            throw new ProviderNotExistException();
//        }
//        System.out.println("ESTOY 3");
//        providerEntity.setUser(userEntity);
//        System.out.println("ESTOY 4 ");
//        return providerRepository.save(providerEntity);
//    }
}