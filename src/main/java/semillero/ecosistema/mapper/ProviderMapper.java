package semillero.ecosistema.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import semillero.ecosistema.dto.ProviderRequestDto;
import semillero.ecosistema.dto.ProviderResponseDto;
import semillero.ecosistema.dto.ProviderUpdateRequestDto;
import semillero.ecosistema.entity.ProviderEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProviderMapper {

    @Mapping(target = "images", ignore = true)
    ProviderRequestDto toDto(ProviderEntity providerEntity);
    @Mapping(target = "images", ignore = true)
    ProviderEntity toEntity(ProviderRequestDto providerDto);
    @Mapping(target = "images", ignore = true)
    ProviderEntity toEntityUpdate(ProviderUpdateRequestDto providerUpdateDto);
    ProviderResponseDto toDtoResponse(ProviderEntity providerEntity);
    List<ProviderResponseDto> toDtoList(List<ProviderEntity> providerEntityList);
}