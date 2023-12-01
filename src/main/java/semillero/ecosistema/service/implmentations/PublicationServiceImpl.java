package semillero.ecosistema.service.implmentations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import semillero.ecosistema.dto.PublicationRequestDto;
import semillero.ecosistema.dto.PublicationResponseDto;
import semillero.ecosistema.entity.PublicationEntity;
import semillero.ecosistema.entity.UserEntity;
import semillero.ecosistema.exception.PublicationNotExistException;
import semillero.ecosistema.mapper.PublicationMapper;
import semillero.ecosistema.repository.PublicationRepository;
import semillero.ecosistema.repository.UserRepository;
import semillero.ecosistema.service.contracts.PublicationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicationServiceImpl  implements PublicationService {
    private final PublicationRepository publicationRepository;
    private final PublicationMapper publicationMapper;
    private final UserRepository userRepository;

    @Override
    public List<PublicationResponseDto> getAll() {
        List<PublicationEntity> publications = publicationRepository.findAll();
        return publications.stream()
                .map(publicationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public PublicationResponseDto getByTitulo(String titulo){
        PublicationEntity publication = publicationRepository.findByTitle(titulo);
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
            incrementViewCount(id);
            return publicationMapper.toResponseDto(publication);
        }else{
            throw new PublicationNotExistException();
        }

    }

    @Override
    public List<PublicationResponseDto> getByDeletedFalse(){
        List<PublicationEntity> publications = publicationRepository.findByHiddenFalse();
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
            publication.setVisualizations(publication.getVisualizations() + 1);
            publicationRepository.save(publication);
        } else {
            throw new PublicationNotExistException();
        }
    }

    @Override
    public PublicationResponseDto save(PublicationRequestDto publicationRequestDto){
        Long user_id = publicationRequestDto.getUser_id();
        Optional<UserEntity> userEntityOptional = userRepository.findById(user_id);
        UserEntity userEntity = null;
        if (userEntityOptional.isPresent()) {
            userEntity = userEntityOptional.get();
        }
        PublicationEntity publicationEntity = publicationMapper.toEntity(publicationRequestDto);
        publicationEntity.setUsuarioCreador(userEntity);
        PublicationEntity savedPublication = publicationRepository.save(publicationEntity);
        return publicationMapper.toResponseDto(savedPublication);
    }

    @Override
    public  PublicationResponseDto update(PublicationRequestDto publicationRequestDto){
        Optional<PublicationEntity> publicationEntityOptional = publicationRepository.findById(publicationRequestDto.getId());

        if (!publicationEntityOptional.isPresent()) {
            throw new PublicationNotExistException();
        }
        List<String> images = new ArrayList<>();
        PublicationEntity publicationEntity = publicationEntityOptional.get();
        publicationEntity.setTitle(publicationRequestDto.getTitle());
        publicationEntity.setContent(publicationRequestDto.getContent());
        publicationEntity.setDate(publicationRequestDto.getDate());
        publicationEntity.setImages(images);
        publicationEntity.setVisualizations(publicationRequestDto.getVisualizations());

        publicationEntity = publicationRepository.save(publicationEntity);

        return publicationMapper.toResponseDto(publicationEntity);
    }

    @Override
    public void delete(Long id){
        Optional<PublicationEntity> publicationEntityOptional = publicationRepository.findById(id);

        if (!publicationEntityOptional.isPresent()) {
            throw new PublicationNotExistException();
        }
        PublicationEntity publicationEntity = publicationEntityOptional.get();
        publicationEntity.setHidden(true);
        publicationRepository.save(publicationEntity);
    }
}
