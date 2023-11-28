package semillero.ecosistema.service.implmentations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import semillero.ecosistema.dto.PublicationRequestDto;
import semillero.ecosistema.dto.PublicationResponseDto;
import semillero.ecosistema.entity.PublicationEntity;
import semillero.ecosistema.exception.PublicationNotExistException;
import semillero.ecosistema.mapper.PublicationMapper;
import semillero.ecosistema.repository.PublicationRepository;
import semillero.ecosistema.service.contracts.PublicationService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicationServiceImpl  implements PublicationService {
    private final PublicationRepository publicationRepository;
    private final PublicationMapper publicationMapper;

    @Override
    public List<PublicationResponseDto> getAll() {
        List<PublicationEntity> publications = publicationRepository.findAll();
        return publications.stream()
                .map(publicationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public PublicationResponseDto getByTitulo(String titulo){
        PublicationEntity publication = publicationRepository.findByTitulo(titulo);
        if (publication != null){
            return publicationMapper.toResponseDto(publication);
        }else{
            throw new PublicationNotExistException();
        }
    }

    @Override
    public PublicationResponseDto getById(Long id){
        Optional<PublicationEntity> publicationOptional = publicationRepository.findById(id);
        if (publicationOptional.isPresent()){
            PublicationEntity publication = publicationOptional.get();
            return publicationMapper.toResponseDto(publication);
        }else{
            throw new PublicationNotExistException();
        }

    }

    @Override
    public List<PublicationResponseDto> getByDeletedFalse(){
        List<PublicationEntity> publications = publicationRepository.findByDeletedFalse();
        return publications.stream()
                .map(publicationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PublicationResponseDto> getByUsuarioId(Long user_id){
        List<PublicationEntity> publications = publicationRepository.findByUsuarioCreadorId(user_id);
        return publications.stream()
                .map(publicationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void incrementViewCount(Long id){
        Optional<PublicationEntity> publicationOptional = publicationRepository.findById(id);
        if (publicationOptional.isPresent()) {
            PublicationEntity publication = publicationOptional.get();
            publication.setCantVisualizaciones(publication.getCantVisualizaciones() + 1);
            publicationRepository.save(publication);
        } else {
            throw new PublicationNotExistException();
        }
    }

    @Override
    public PublicationResponseDto save(PublicationRequestDto publicationRequestDto){
        PublicationEntity publicationEntity = publicationMapper.toEntity(publicationRequestDto);
        PublicationEntity savedPublication = publicationRepository.save(publicationEntity);
        return publicationMapper.toResponseDto(savedPublication);
    }
}
