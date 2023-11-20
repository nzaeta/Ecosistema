package semillero.ecosistema.service.implmentations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import semillero.ecosistema.Dto.ProviderResponseDto;
import semillero.ecosistema.entity.ProviderEntity;
import semillero.ecosistema.entity.UserEntity;
import semillero.ecosistema.enums.ProviderEnum;
import semillero.ecosistema.exception.ProviderMaxCreatedException;
import semillero.ecosistema.exception.ProviderNotExistException;
import semillero.ecosistema.exception.UserNotExistException;
import semillero.ecosistema.mapper.ProviderMapper;
import semillero.ecosistema.repository.CategoryRepository;
import semillero.ecosistema.repository.ProviderRepository;
import semillero.ecosistema.repository.UserRepository;
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

    private static final String STATUS_INITIAL = ProviderEnum.REVISION_INICIAL.name();

    @Override
    public List<ProviderResponseDto> getAll() {
        System.out.println("ESTOY");
        List<ProviderEntity> providerEntityList = providerRepository.findAll();
        System.out.println("ESTOY ACA");
        List<ProviderResponseDto> providerResponseDtoList = providerMapper.toDtoList(providerEntityList);
        System.out.println("ESTOY ACA 2");
        mapperParamsProvider(providerEntityList, providerResponseDtoList);
        System.out.println("ESTOY ACA 3");
        return providerResponseDtoList;
    }

    @Override
    public List<ProviderResponseDto> getByName(String name) {
        List<ProviderEntity> providerEntityList = providerRepository.findByName(name);
        List<ProviderResponseDto> providerResponseDtoList = providerMapper.toDtoList(providerEntityList);

        mapperParamsProvider(providerEntityList, providerResponseDtoList);
        return providerResponseDtoList;
    }

    /**
     * obtener nombre de: categoria, provincia y pais,
     * parar mapearlo a la lista del Dto Response
     */
    private void mapperParamsProvider(List<ProviderEntity> providerEntityList, List<ProviderResponseDto> providerResponseDtoList) {
        providerResponseDtoList.stream().forEach(providerResponseDto -> {
            providerEntityList.stream().filter(providerEntity -> providerEntity.getId() == providerResponseDto.getId())
                    .forEach(provider -> {
                        String categoryName = categoryRepository.findById(provider.getCategoryId()).get().getNombre();
                        //String countryName = categoryRepository.findById(provider.getCountryId()).get().getNombre();
                        //String provinceName = categoryRepository.findById(provider.getProvinceId()).get().getNombre();

                        providerResponseDto.setCategoryName(categoryName);
                        //providerResponseDto.setCountryName(countryName);
                        //providerResponseDto.setProvinceName(provinceName);
                    });
        });
    }


    @Override
    public ProviderEntity save(Long userId, ProviderEntity providerEntity) {
        UserEntity userEntity = getUsersById(userId);

        validateMaxProviders(userEntity);

        parametersInitialProvider(providerEntity, userEntity);

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

    @Override
    public ProviderEntity update(Long userId, ProviderEntity providerEntity) {
        UserEntity userEntity = getUsersById(userId);
        System.out.println("ESTOY");
        Optional<ProviderEntity> existProvider = providerRepository.findById(providerEntity.getId());
        System.out.println("ESTOY 2 " );
        if(existProvider.isEmpty()) {
            throw new ProviderNotExistException();
        }
        System.out.println("ESTOY 3");
        providerEntity.setUser(userEntity);
        System.out.println("ESTOY 4 ");
        return providerRepository.save(providerEntity);
    }
}