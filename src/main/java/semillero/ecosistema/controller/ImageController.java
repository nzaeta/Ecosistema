package semillero.ecosistema.controller;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import semillero.ecosistema.entity.ImageEntity;
import semillero.ecosistema.service.CloudinaryService;
import semillero.ecosistema.service.contracts.ImageService;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import semillero.ecosistema.Dto.MsjImagenDto;
@RequestMapping("/imagen")
@RestController
@RequiredArgsConstructor
public class ImageController {
    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    ImageService imagenService;


    @GetMapping("/list")
    public ResponseEntity<List<ImageEntity>> list(){
        List<ImageEntity> list = imagenService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam MultipartFile multipartFile)throws IOException {
        BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
        if(bi == null){
            return new ResponseEntity(new MsjImagenDto ("Imagen no válida."), HttpStatus.BAD_REQUEST);
        }
        java.util.Map result = cloudinaryService.upload(multipartFile);
        ImageEntity imagen = new ImageEntity();
        imagen.setName((String) result.get("original_filename"));
        imagen.setImagenUrl((String) result.get("url"));
        imagen.setImagenId((String) result.get("public_id"));


        imagenService.save(imagen);
        return new ResponseEntity(new MsjImagenDto("Imagen subida."), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id)throws IOException {
        if(!imagenService.exists(id))
            return new ResponseEntity(new MsjImagenDto("Imagen inexistente."), HttpStatus.NOT_FOUND);
        ImageEntity imagen = imagenService.getImagen(id).get();
        Map result = cloudinaryService.delete(imagen.getImagenId());
        imagenService.delete(id);
        return new ResponseEntity(new MsjImagenDto("Imagen eliminada"), HttpStatus.OK);

    }
}
    /*Imagen imagen = imagenService.getImagen(id).get();
    Map result = cloudinaryService.delete(imagen.getCloudinaryId());
		imagenService.delete(id);*/


