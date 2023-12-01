package semillero.ecosistema.service.contracts;

import semillero.ecosistema.dto.PublicationRequestDto;
import semillero.ecosistema.dto.PublicationResponseDto;
import semillero.ecosistema.entity.PublicationEntity;

import java.util.List;

public interface PublicationService {
    List<PublicationResponseDto> getAll();
    PublicationResponseDto getByTitulo(String titulo);
    PublicationResponseDto getById(Long id);
    List<PublicationResponseDto> getByDeletedFalse();
    List<PublicationResponseDto> getByUsuarioId(Long user_id);
//    void incrementViewCount(Long id);
    PublicationResponseDto save(PublicationRequestDto publicationRequestDto);

    PublicationResponseDto update(PublicationRequestDto publicationRequestDto);

    void delete(Long id);

    void incrementViewCount(Long id);

}
