package semillero.ecosistema.mapper;

import org.mapstruct.Mapper;
import semillero.ecosistema.dto.ProviderRequestDto;
import semillero.ecosistema.dto.ProviderResponseDto;
import semillero.ecosistema.dto.ProviderUpdateRequestDto;
import semillero.ecosistema.entity.ProviderEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProviderMapper {

    ProviderRequestDto toDto(ProviderEntity providerEntity);

    ProviderEntity toEntity(ProviderRequestDto providerDto);

    ProviderEntity toEntityUpdate(ProviderUpdateRequestDto providerUpdateDto);

    List<ProviderResponseDto> toDtoList(List<ProviderEntity> providerEntityList);
}