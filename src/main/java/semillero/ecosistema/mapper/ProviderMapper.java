package semillero.ecosistema.mapper;

import org.mapstruct.Mapper;
import semillero.ecosistema.Dto.ProviderRequestDto;
import semillero.ecosistema.Dto.ProviderResponseDto;
import semillero.ecosistema.Dto.ProviderUpdateRequestDto;
import semillero.ecosistema.entity.ProviderEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProviderMapper {

    ProviderRequestDto toDto(ProviderEntity providerEntity);

    ProviderEntity toEntity(ProviderRequestDto providerDto);

    ProviderEntity toEntityUpdate(ProviderUpdateRequestDto providerUpdateDto);

    List<ProviderResponseDto> toDtoList(List<ProviderEntity> providerEntityList);
}