package semillero.ecosistema.mapper;


import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import semillero.ecosistema.dto.ImageDto;
import semillero.ecosistema.dto.PublicationRequestDto;
import semillero.ecosistema.dto.PublicationResponseDto;
import semillero.ecosistema.entity.ImageEntity;
import semillero.ecosistema.entity.PublicationEntity;
import semillero.ecosistema.entity.UserEntity;
import semillero.ecosistema.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

            List<ImageDto> imageDtos = new ArrayList<>();
            for(ImageEntity image:publicationEntity.getImagenes()){
                ImageDto imageDto = new ImageDto();
                imageDto.setId(image.getId());
                imageDto.setName(image.getName());
                imageDto.setImagenUrl(image.getImagenUrl());
                imageDto.setCloudinaryId(image.getCloudinaryId());
                imageDto.setProvider(image.getProvider());
                imageDto.setPublication(image.getPublicationEntity());
                imageDtos.add(imageDto);
            }
            List<ImageEntity> list = publicationEntity.getImagenes();
            if ( list != null ) {
                publicationResponseDto.setImages( imageDtos );
            }
        }
    }



}
