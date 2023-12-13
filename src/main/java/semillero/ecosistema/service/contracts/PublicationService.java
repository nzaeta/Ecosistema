package semillero.ecosistema.service.contracts;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import semillero.ecosistema.Dto.PublicationRequestDto;
import semillero.ecosistema.Dto.PublicationResponseDto;
import semillero.ecosistema.entity.PublicationEntity;

import java.util.List;

public interface PublicationService {
    ResponseEntity<?> getAll();
    ResponseEntity<?> getByTitle(String title);
    ResponseEntity<?> getById(String id);
    ResponseEntity<?> getByDeletedFalse();
    ResponseEntity<?> getByUsuarioId(String user_id);
    ResponseEntity<?> save(PublicationRequestDto publicationRequestDto);
    ResponseEntity<?> update(PublicationRequestDto publicationRequestDto);
    ResponseEntity<?> delete(String id);
    ResponseEntity<?> active(String id);
    void incrementViewCount(String id);
    void agregarImagenAPublicacion(String id, MultipartFile imagen);

}
