package semillero.ecosistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semillero.ecosistema.entity.ProvinceEntity;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<ProvinceEntity, Long> {
    ProvinceEntity findByName(String name);
    List<ProvinceEntity> findByCountryId(Long country_id);
}
