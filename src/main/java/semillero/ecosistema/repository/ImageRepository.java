package semillero.ecosistema.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semillero.ecosistema.entity.ImageEntity;

import java.util.List;

@Repository
    public interface ImageRepository extends JpaRepository<ImageEntity, String> {
        List<ImageEntity> findByOrderById();
        List<ImageEntity> findByProviderId(String providerId);
    }

