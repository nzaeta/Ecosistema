package semillero.ecosistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import semillero.ecosistema.entity.ProviderEntity;

import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<ProviderEntity, String> {
    List<ProviderEntity> findByName(String name);
    List<ProviderEntity> findByStatus(String status);

    @Query("SELECT a FROM ProviderEntity a WHERE a.name LIKE %:nombre% and a.deleted = false")
    public List<ProviderEntity> searchProviderByName(@Param("nombre") String nombre);

    @Query("SELECT a FROM ProviderEntity a WHERE a.deleted = false and status = 'ACEPTADO'")
    public List<ProviderEntity> listarAceptados();

    @Query("SELECT a FROM ProviderEntity a WHERE a.deleted = false and a.category.nombre = :categoria")
    public List<ProviderEntity> listarPorCategoria(@Param("categoria") String categoria);

    @Query(value = "SELECT * FROM proveedores p WHERE (p.status = :REVISION_INICIAL OR p.status = :CAMBIOS_REALIZADOS) and p.deleted = false", nativeQuery = true)
    List<ProviderEntity> findAllByStatus(String REVISION_INICIAL, String CAMBIOS_REALIZADOS);

}