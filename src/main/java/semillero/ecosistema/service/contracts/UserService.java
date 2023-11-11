package semillero.ecosistema.service.contracts;

import semillero.ecosistema.entity.UserEntity;

import java.util.List;

public interface UserService {

    List<UserEntity> getAll();

    List<UserEntity> getAllByRol(String rol);

    UserEntity getByEmail(String email);

    UserEntity save(UserEntity userEntity);

    UserEntity update(String email, UserEntity userEntity);

    Boolean disabledUserById(Long id);

    Boolean disabledUserByEmail(String email);
}