package semillero.ecosistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semillero.ecosistema.entity.CountryEntity;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity,String> {
    CountryEntity findByName(String name);
}
