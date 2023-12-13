package semillero.ecosistema.service.contracts;

import semillero.ecosistema.Dto.ProviderRequestDto;
import semillero.ecosistema.Dto.ProviderResponseDto;
import semillero.ecosistema.Dto.ProviderUpdateStatusRequestDto;
import semillero.ecosistema.Dto.ProviderUpdateRequestDto;
import semillero.ecosistema.entity.ProviderEntity;

import java.util.List;

public interface ProviderService {

    List<ProviderResponseDto> getAll();

    List<ProviderResponseDto> getByName(String name);

    List<ProviderResponseDto> getByCategory(String category);

    List<ProviderResponseDto> getAccepted();

    ProviderEntity save(String userId, ProviderRequestDto providerRequestDto);

    List<ProviderResponseDto> getByStatus();
    Boolean updateStatus(ProviderUpdateStatusRequestDto providerUpdateStatusRequestDto);
    ProviderEntity update(ProviderUpdateRequestDto providerUpdateRequestDto);

    List<ProviderResponseDto> getByLocation(double latitude, double longitude);
}