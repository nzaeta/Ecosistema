package semillero.ecosistema.service.contracts;

import org.springframework.http.ResponseEntity;
import semillero.ecosistema.Dto.PublicationRequestDto;
import semillero.ecosistema.Dto.PublicationResponseDto;
import semillero.ecosistema.entity.PublicationEntity;

import java.util.List;

public interface PublicationService {
    ResponseEntity<?> getAll();
    ResponseEntity<?> getByTitle(String title);
    ResponseEntity<?> getById(Long id);
    ResponseEntity<?> getByDeletedFalse();
    ResponseEntity<?> getByUsuarioId(Long user_id);
    ResponseEntity<?> save(PublicationRequestDto publicationRequestDto);
    ResponseEntity<?> update(PublicationRequestDto publicationRequestDto);
    ResponseEntity<?> delete(Long id);
    void incrementViewCount(Long id);

}
