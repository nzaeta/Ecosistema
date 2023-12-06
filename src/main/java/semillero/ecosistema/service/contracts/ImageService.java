package semillero.ecosistema.service.contracts;

import semillero.ecosistema.entity.ImageEntity;

import java.util.List;
import java.util.Optional;

public interface ImageService {

    List<ImageEntity> list();
    Optional<ImageEntity> getImagen(String id);
    ImageEntity save(ImageEntity imagen);

    public void delete (String id);

    boolean exists(String id);

}
