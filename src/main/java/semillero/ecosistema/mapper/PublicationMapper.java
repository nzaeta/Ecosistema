package semillero.ecosistema.mapper;


import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import semillero.ecosistema.Dto.PublicationRequestDto;
import semillero.ecosistema.Dto.PublicationResponseDto;
import semillero.ecosistema.entity.PublicationEntity;
import semillero.ecosistema.entity.UserEntity;
import semillero.ecosistema.repository.UserRepository;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PublicationMapper {
    @Mapping(target = "usuarioCreador", ignore = true)
    PublicationEntity toEntity(PublicationRequestDto publicationDto);

//    @AfterMapping
//    default void setUser(PublicationRequestDto publicationDto, @MappingTarget PublicationEntity publicationEntity, UserRepository userRepository){
//        if(publicationDto.getUser_id() != null){
//            UserEntity userEntity = userRepository.findById(publicationDto.getUser_id()).orElse(null);
//            publicationEntity.setUsuarioCreador(userEntity);
//        }
//    }

    @Mapping(target = "userName", ignore = true)
    PublicationResponseDto toResponseDto(PublicationEntity publicationEntity);

    @AfterMapping
    default void setUserName(PublicationEntity publicationEntity, @MappingTarget PublicationResponseDto publicationResponseDto){
        if(publicationEntity.getUsuarioCreador() != null){
            publicationResponseDto.setUserName(publicationEntity.getUsuarioCreador().getNombre());
        }
    }

}
