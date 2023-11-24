package semillero.ecosistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import semillero.ecosistema.entity.ProviderEntity;

import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<ProviderEntity, Long> {
    List<ProviderEntity> findByName(String name);

    @Query("SELECT a FROM ProviderEntity a WHERE a.name LIKE %:nombre% and a.deleted = false and a.active = true")
    public List<ProviderEntity> searchProviderByName(@Param("nombre") String nombre);
}