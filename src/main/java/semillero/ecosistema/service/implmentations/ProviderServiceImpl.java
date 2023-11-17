package semillero.ecosistema.service.implmentations;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import semillero.ecosistema.constants.ProviderEnum;
import semillero.ecosistema.entity.ProviderEntity;
import semillero.ecosistema.entity.UserEntity;
import semillero.ecosistema.exception.ProviderMaxCreatedException;
import semillero.ecosistema.exception.ProviderNotExistException;
import semillero.ecosistema.exception.UserNotExistException;
import semillero.ecosistema.repository.ProviderRepository;
import semillero.ecosistema.repository.UserRepository;
import semillero.ecosistema.service.contracts.ProviderService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;

    @Override
    public ProviderEntity save(Long userId, ProviderEntity providerEntity) {
        /*if (providerEntity == null) {
            throw new IllegalArgumentException("El proveedor no puede ser nulo");
        }

        Integer maxCreatedProvider = providerRepository.countByUserId(providerEntity.getUserId());
        if(maxCreatedProvider > 3) {
            throw new ProviderMaxCreatedException();
        }
        */

        System.out.println("Usuario ID -> " + userId);

        //UserEntity userEntity = userRepository.findById(userId).get();
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        System.out.println("Usuario -> " + userEntity.getEmail());

        if(userEntity == null) {
            System.out.println("ERROR USUARIO NULO");
            throw new UserNotExistException();
        }

        Integer maxCreatedProvider = userEntity.getProviderEntityList().size();

        System.out.println("Usuario 2 -> " + userEntity.getProviderEntityList().size());

        if(maxCreatedProvider > 3) {
            throw new ProviderMaxCreatedException();
        }

        String statusInitial = ProviderEnum.REVISION_INICIAL.name();
        providerEntity.setStatus(statusInitial);

        providerEntity.setUsers(userEntity);

        //userEntity.setProviderEntityList(List.of(providerEntity));

        userEntity.getProviderEntityList().add(providerEntity);

        //userRepository.save(userEntity);

        System.out.println("Usuario YA PROVEDIR -> " + userEntity.getProviderEntityList().size());

        System.out.println("Usuario en proveedor -> " +providerEntity.getUsers().getNombre());

        //ProviderEntity providerSaved = providerRepository.save(providerEntity);
        return providerEntity;
    }

    @Override
    public ProviderEntity update(Long userId, ProviderEntity providerEntity) {
        Optional<ProviderEntity> providerExist = providerRepository.findById(providerEntity.getId());
        if(providerExist.isPresent()) {
            return providerRepository.save(providerEntity);
        } else {
            throw new EntityNotFoundException("Proveedor no encontrado");
        }
    }

    @Override
    public Optional<ProviderEntity> updateByUserId(ProviderEntity providerEntity) {
        /*List<ProviderEntity> providerEntityList = providerRepository.findAllByUserId(providerEntity.getUserId());
        if(providerEntityList.isEmpty()) {
            throw new ProviderNotExistException();
        }
        return Optional.of(providerRepository.save(providerEntity));*/
        return null;
    }
}
