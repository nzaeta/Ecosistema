package semillero.ecosistema.service.implmentations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import semillero.ecosistema.dto.PublicationRequestDto;
import semillero.ecosistema.dto.PublicationResponseDto;
import semillero.ecosistema.dto.PublicationUpdateRequestDto;
import semillero.ecosistema.entity.ImageEntity;
import semillero.ecosistema.entity.PublicationEntity;
import semillero.ecosistema.entity.UserEntity;
import semillero.ecosistema.exception.*;
import semillero.ecosistema.mapper.PublicationMapper;
import semillero.ecosistema.repository.PublicationRepository;
import semillero.ecosistema.repository.UserRepository;
import semillero.ecosistema.service.CloudinaryService;
import semillero.ecosistema.service.contracts.ImageService;
import semillero.ecosistema.service.contracts.PublicationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicationServiceImpl  implements PublicationService {
    private final PublicationRepository publicationRepository;
    private final PublicationMapper publicationMapper;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final ImageService imageService;

    @Override
    public ResponseEntity<?> getAll() {
        try {
            List<PublicationEntity> publications = publicationRepository.findAllByOrderByDateDesc();
            List<PublicationResponseDto> publicationsDto = publications.stream()
                    .map(publicationMapper::toResponseDto)
                    .collect(Collectors.toList());

            if(publicationsDto.isEmpty()){
                List<?> listnull = new ArrayList<>();
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(publicationsDto);
        } catch (Exception e){

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @Override
    public ResponseEntity<?> getByTitle(String title){
        try {
            PublicationEntity publication = publicationRepository.findByTitleOrderByDateDesc(title);
            if (publication == null) {
                throw new PublicationNotExistException();
            }
            PublicationResponseDto publicationDto = publicationMapper.toResponseDto(publication);
            return ResponseEntity.ok(publicationDto);
        }catch (PublicationNotExistException e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The publication does not exist with this title: " + title);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e);
        }
    }

    @Override
    public ResponseEntity<?> getById(String id){
        try {
            PublicationEntity publication = publicationRepository.findById(id)
                    .orElseThrow(PublicationNotExistException::new);
            PublicationResponseDto publicationDto = publicationMapper.toResponseDto(publication);
            return ResponseEntity.ok(publicationDto);
        }catch (PublicationNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The publication does not exist with this ID: " + id);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e);
        }

    }

    @Override
    public ResponseEntity<?> getByDeletedFalse(){
        try {
            List<PublicationEntity> publications = publicationRepository.findByHiddenFalseOrderByDateDesc();
            List<PublicationResponseDto> publicationsDto = publications.stream()
                    .map(publicationMapper::toResponseDto)
                    .collect(Collectors.toList());
            if(publicationsDto.isEmpty()){
                List<?> listnull = new ArrayList<>();
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(listnull);
            }
            return ResponseEntity.ok(publicationsDto);
        }catch (Exception e){

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @Override
    public ResponseEntity<?> getByUsuarioId(String user_id){
        try {
            List<PublicationEntity> publications = publicationRepository.findByUsuarioCreadorIdOrderByDateDesc(user_id);
            List<PublicationResponseDto> publicationsDto = publications.stream()
                    .map(publicationMapper::toResponseDto)
                    .collect(Collectors.toList());
            if(publicationsDto.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(publicationsDto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }

    }

    @Override
    public void incrementViewCount(String id){
            PublicationEntity publication = publicationRepository.findById(id)
                    .orElseThrow(PublicationNotExistException::new);
            publication.setVisualizations(publication.getVisualizations() + 1);
            publicationRepository.save(publication);
    }


    @Override
    public ResponseEntity<?> save(PublicationRequestDto publicationRequestDto){
        try {
            UserEntity user = userRepository.findById(publicationRequestDto.getUser_id())
                    .orElseThrow(UserNotExistException::new);
            PublicationEntity publication = publicationMapper.toEntity(publicationRequestDto);
            publication.setUsuarioCreador(user);
            List<MultipartFile> list = publicationRequestDto.getImages();
            List<ImageEntity> imagenes = agregarImagenAPublicacion(list);
            publication.setImagenes(imagenes);
            publicationRepository.save(publication);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (UserNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body("User ID not found");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }
// si funciona el otro update, este se borra
    @Override
    @Transactional
    public ResponseEntity<?> update(PublicationRequestDto publicationRequestDto) {
        try {
            PublicationEntity publication = publicationRepository.findById(publicationRequestDto.getId())
                    .orElseThrow(PublicationNotExistException::new);

           // List<String> images = new ArrayList<>();
            publication.setTitle(publicationRequestDto.getTitle());
            publication.setContent(publicationRequestDto.getContent());
            publication.setDate(publicationRequestDto.getDate());
            publication.setVisualizations(publicationRequestDto.getVisualizations());
            List<ImageEntity> imagenes = agregarImagenAPublicacion(publicationRequestDto.getImages());
            publication.getImagenes().clear();
            publication.getImagenes().addAll(imagenes);
            publicationRepository.save(publication);
            return ResponseEntity.ok().body("UPDATED");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @Override
    public ResponseEntity<?> agregarImagen(PublicationRequestDto publicationRequestDto) throws IOException {
        return null;
    }

    @Transactional
    @Override
    public ResponseEntity<?> updatePublication(PublicationUpdateRequestDto publicationUpdateRequestDto) {
        try {
            PublicationEntity publication = publicationRepository.findById(publicationUpdateRequestDto.getId())
                    .orElseThrow(PublicationNotExistException::new);
            publication.setTitle(publicationUpdateRequestDto.getTitle());
            publication.setContent(publicationUpdateRequestDto.getContent());
            publication.setDate(publicationUpdateRequestDto.getDate());
            publication.setVisualizations(publicationUpdateRequestDto.getVisualizations());
            List<ImageEntity> imagenes = modificarImagenEnPublicacion(publicationUpdateRequestDto, publication);
            publication.getImagenes().addAll(imagenes);
            publicationRepository.save(publication);
            return ResponseEntity.ok().body("UPDATED");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @Transactional
    public List<ImageEntity>  modificarImagenEnPublicacion
            (PublicationUpdateRequestDto publicationUpdateRequestDto, PublicationEntity publication) throws IOException {
        List<String> imagenesParaBorrar = new ArrayList<>(publicationUpdateRequestDto.getImagenesParaBorrar());
        for (String id : imagenesParaBorrar) {
            ImageEntity imagen = imageService.getImagen(id).get();
            publication.getImagenes().remove(imagen);
            cloudinaryService.delete(imagen.getCloudinaryId());
            imageService.delete(id);
        }

        List<ImageEntity> listaImagen = agregarImagenAPublicacion(publicationUpdateRequestDto.getImagenesNuevas());
        return listaImagen;
    }
/*
                for (MultipartFile imagen: publicationUpdateRequestDto.getImagenesNuevas()) {

                        // Subir la imagen a Cloudinary
                        Map subirImagen = cloudinaryService.upload(imagen);

                        //Crea la imagen en la BD
                        ImageEntity image = new ImageEntity();
                        image.setName((String) subirImagen.get("original_filename"));
                        image.setImagenUrl((String) subirImagen.get("url"));
                        image.setCloudinaryId((String) subirImagen.get("public_id"));
                        listaImagen.add(image);
                }
           return listaImagen;
*/



/*

    @Override
    public ResponseEntity<?> agregarImagen(PublicationRequestDto publicationRequestDto) throws IOException {
        List<ImageEntity> listaImagen = new ArrayList<>();
        try {
            for (MultipartFile imagen: publicationRequestDto.getImages()) {
                if (validateImageSize(imagen)) {
                    // Subir la imagen a Cloudinary
                    Map subirImagen = cloudinaryService.upload(imagen);

                    // Crear y guardar la entidad de Imagen
                    ImageEntity image = new ImageEntity();

                    image.setName((String) subirImagen.get("original_filename"));
                    image.setImagenUrl((String) subirImagen.get("url"));
                    image.setCloudinaryId((String) subirImagen.get("public_id"));
//                     ImageEntity im = imageService.save(image);
                    listaImagen.add(image);


                } else {
                    return new ResponseEntity<>("El tamaño de la imagen supera 1M", HttpStatusCode.valueOf(400));

                }
            }
        } catch (IOException e) {
            return new ResponseEntity<>("Error al cargar la imagen", HttpStatusCode.valueOf(400));

        }
        return new ResponseEntity<>(listaImagen, HttpStatusCode.valueOf(200));
    }

    public boolean validateImageSize(MultipartFile file) {
        try {
            // Obtiene el tamaño del archivo en bytes
            long fileSizeInBytes = file.getSize();
            // Verifica si el tamaño es superior a 1 MB
            if (fileSizeInBytes > 1 * 1024 * 1024) {
                return false;
            }
            return true;

        } catch (ExcessImageSizeException e) {
            return false; // Tamaño no válido
        }
    }

*/
    //Este es el método que se usa en el SAVE
    public List<ImageEntity> agregarImagenAPublicacion (List <MultipartFile> lista) throws IOException {

//             PublicationEntity publicacion = publicationRepository.findById(publicationRequestDto.getId())
//                     .orElseThrow(PublicationNotExistException::new);
//             validarLimiteDeImagenes(publicacion);
            List<ImageEntity> listaImagen = new ArrayList<>();
              try {
                 for (MultipartFile imagen: lista) {

                     // Subir la imagen a Cloudinary
                     Map subirImagen = cloudinaryService.upload(imagen);

                     // Crear y guardar la entidad de Imagen
                     ImageEntity image = new ImageEntity();

                     image.setName((String) subirImagen.get("original_filename"));
                     image.setImagenUrl((String) subirImagen.get("url"));
                     image.setCloudinaryId((String) subirImagen.get("public_id"));
//                     ImageEntity im = imageService.save(image);
                     listaImagen.add(image);
                 }
               return listaImagen;
             } catch (IOException e) {
                 throw new RuntimeException("Error al cargar la imagen.");
             }
         }

   /* private void validarLimiteDeImagenes(@NotNull PublicationEntity publicacion) {
        if (publicacion.getImagenes().size() > 3 || publicacion.getImagenes().isEmpty()) {
            throw new ImagenesPorPublicacionException();
        }
    }
*/

    @Override
    public ResponseEntity<?> delete(String id){
        try {
            PublicationEntity publication = publicationRepository.findById(id)
                    .orElseThrow(PublicationNotExistException::new);
            publication.setHidden(true);
            publicationRepository.save(publication);
            return ResponseEntity.status(HttpStatus.OK).body("HIDDEN");
        }catch (PublicationNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publication Not Found");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @Override
    public ResponseEntity<?> active(String id){
        try {
            PublicationEntity publication = publicationRepository.findById(id)
                    .orElseThrow(PublicationNotExistException::new);
            publication.setHidden(false);
            publicationRepository.save(publication);
            return ResponseEntity.status(HttpStatus.OK).body("ACTIVED");
        }catch (PublicationNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }
}
