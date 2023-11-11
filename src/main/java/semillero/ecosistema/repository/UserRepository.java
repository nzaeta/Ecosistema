package semillero.ecosistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semillero.ecosistema.entity.UserEntity;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    List<UserEntity> findByNombreAndApellido(String nombre, String apellido);
    UserEntity findByEmail(String email);
    List<UserEntity> findByRol(String rol);

    void deleteByEmail(String email);

    List<UserEntity> findAllByOrderByNombreAsc();


}
