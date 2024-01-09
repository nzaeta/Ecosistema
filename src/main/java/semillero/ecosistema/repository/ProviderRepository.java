package semillero.ecosistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import semillero.ecosistema.entity.ProviderEntity;

import java.util.List;
import java.util.Map;

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

    @Query("SELECT p FROM ProviderEntity p WHERE (p.country.name = :pais and p.province.name = :provincia and p.city = :ciudad) and p.deleted = false")
    List<ProviderEntity> findByLocation(String pais, String provincia, String ciudad);

    @Query("SELECT a FROM ProviderEntity a WHERE a.deleted = false and a.user.email = :usuario")
    public List<ProviderEntity> listarPorUsuario(@Param("usuario") String usuario);

    @Query("SELECT c.nombre AS categoryName, COUNT(p.id) AS providerCount FROM ProviderEntity p JOIN p.category c GROUP BY c.nombre")
    List<Object[]> getProvidersPerCategory();


}