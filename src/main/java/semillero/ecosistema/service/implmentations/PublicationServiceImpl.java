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

    @Transactional
    @Override
    public ResponseEntity<?> updatePublication(PublicationUpdateRequestDto publicationUpdateRequestDto) {
        if (publicationUpdateRequestDto.isImagenesBorrarVacio()){
            publicationUpdateRequestDto.setImagenesParaBorrar(new ArrayList<>());
        }
        if (publicationUpdateRequestDto.isImagenesNuevasVacio()){
            publicationUpdateRequestDto.setImagenesNuevas(new ArrayList<>());
        }

        try {
            PublicationEntity publication = publicationRepository.findById(publicationUpdateRequestDto.getId())
                    .orElseThrow(PublicationNotExistException::new);
            publication.setTitle(publicationUpdateRequestDto.getTitle());
            publication.setContent(publicationUpdateRequestDto.getContent());
            publication.setDate(publicationUpdateRequestDto.getDate());
            publication.setVisualizations(publicationUpdateRequestDto.getVisualizations());
            modificarImagenEnPublicacion(publicationUpdateRequestDto, publication);
            publicationRepository.save(publication);
            return ResponseEntity.ok().body("UPDATED");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @Transactional
    public void  modificarImagenEnPublicacion
            (PublicationUpdateRequestDto publicationUpdateRequestDto, PublicationEntity publication) throws IOException {
        List<String> imagenesParaBorrar = new ArrayList<>(publicationUpdateRequestDto.getImagenesParaBorrar());
        for (String id : imagenesParaBorrar) {
            ImageEntity imagen = imageService.getImagen(id).get();
            publication.getImagenes().remove(imagen);
            cloudinaryService.delete(imagen.getCloudinaryId());
            imageService.delete(id);
        }
        List <MultipartFile> lista = publicationUpdateRequestDto.getImagenesNuevas();
        if (lista.size() > 0){
            List<ImageEntity> listaImagen = agregarImagenAPublicacion(lista);
            publication.getImagenes().addAll(listaImagen);
        }

    }
    //Este es el método que se usa en el SAVE y Update
    public List<ImageEntity> agregarImagenAPublicacion (List <MultipartFile> lista) throws IOException {
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
