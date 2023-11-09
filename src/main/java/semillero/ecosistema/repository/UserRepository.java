package semillero.ecosistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semillero.ecosistema.entity.UserEntity;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    public List<UserEntity> findByNombreAndApellido(String nombre, String apellido);
    public UserEntity findByEmail(String email);
    public List<UserEntity> findByRol(String rol);

    public void deleteByEmail(String email);

    public List<UserEntity> findAllByOrderByNombreAsc();

}
