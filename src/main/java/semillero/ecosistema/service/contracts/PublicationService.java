package semillero.ecosistema.service.contracts;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import semillero.ecosistema.dto.PublicationRequestDto;
import semillero.ecosistema.dto.PublicationResponseDto;
import semillero.ecosistema.dto.PublicationUpdateRequestDto;
import semillero.ecosistema.entity.PublicationEntity;

import java.io.IOException;
import java.util.List;

public interface PublicationService {
    ResponseEntity<?> getAll();
    ResponseEntity<?> getByTitle(String title);
    ResponseEntity<?> getById(String id);
    ResponseEntity<?> getByDeletedFalse();
    ResponseEntity<?> getByUsuarioId(String user_id);
    ResponseEntity<?> save(PublicationRequestDto publicationRequestDto);
    ResponseEntity<?> update(PublicationRequestDto publicationRequestDto);

    ResponseEntity<?> agregarImagen(PublicationRequestDto publicationRequestDto) throws IOException;

    @Transactional
    ResponseEntity<?> updatePublication(PublicationUpdateRequestDto publicationUpdateRequestDto);

    ResponseEntity<?> delete(String id);
    ResponseEntity<?> active(String id);
    void incrementViewCount(String id);


}
