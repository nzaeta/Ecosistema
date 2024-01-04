package semillero.ecosistema.service.implmentations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semillero.ecosistema.entity.ImageEntity;
import semillero.ecosistema.repository.ImageRepository;
import semillero.ecosistema.service.contracts.ImageService;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    ImageRepository imagenRepository;

    public List<ImageEntity> list(){
        return imagenRepository.findByOrderById();
    }

    public Optional<ImageEntity> getOne(String id){return imagenRepository.findById(id);}

    public Optional<ImageEntity> getImagen(String id){
        return imagenRepository.findById(id);
    }
    public ImageEntity save(ImageEntity imagen){return imagenRepository.save(imagen);}

    public void delete(String id){
        imagenRepository.deleteById(id);
    }

    public boolean exists(String id){
        return imagenRepository.existsById(id);
    }
}

