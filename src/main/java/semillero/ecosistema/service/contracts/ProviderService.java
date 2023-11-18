package semillero.ecosistema.service.contracts;

import semillero.ecosistema.Dto.ProviderResponseDto;
import semillero.ecosistema.entity.ProviderEntity;

import java.util.List;

public interface ProviderService {

    List<ProviderResponseDto> getAll();

    List<ProviderResponseDto> getByName(String name);

    ProviderEntity save(Long userId, ProviderEntity providerEntity);

    ProviderEntity update(Long userId,ProviderEntity providerEntity);
}