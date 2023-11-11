package semillero.ecosistema.service.implmentations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import semillero.ecosistema.entity.UserEntity;
import semillero.ecosistema.exception.EmailExistException;
import semillero.ecosistema.exception.UserNotExistException;
import semillero.ecosistema.repository.UserRepository;
import semillero.ecosistema.service.contracts.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<UserEntity> getAllByRol(String rol) {
        List<UserEntity> userEntityList = userRepository.findByRol(rol);
        return userEntityList;
    }

    @Override
    public UserEntity getByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        return userEntity;
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        if(getByEmail(userEntity.getEmail()) != null) {
            throw new EmailExistException();
        }
        userEntity.setDeleted(false);
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity update(String email, UserEntity userEntity) {
        UserEntity user = getByEmail(email);
        if(user == null) {
            throw new UserNotExistException();
        }
        userEntity.setId(user.getId());
        return userRepository.save(userEntity);
    }

    @Override
    public Boolean disabledUserById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(userEntity.isEmpty()) {
            return false;
        }
        userEntity.get().setDeleted(true);
        userRepository.save(userEntity.get());
        return true;
    }

    @Override
    public Boolean disabledUserByEmail(String email) {
        UserEntity userEntity = getByEmail(email);
        if(userEntity == null) {
            return false;
        }
        userEntity.setDeleted(true);
        userRepository.save(userEntity);
        return true;
    }
}