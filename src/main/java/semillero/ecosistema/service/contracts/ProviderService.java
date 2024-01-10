package semillero.ecosistema.service.contracts;

import org.springframework.http.ResponseEntity;
import semillero.ecosistema.dto.ProviderRequestDto;
import semillero.ecosistema.dto.ProviderResponseDto;
import semillero.ecosistema.dto.ProviderUpdateStatusRequestDto;
import semillero.ecosistema.dto.ProviderUpdateRequestDto;
import semillero.ecosistema.entity.ProviderEntity;

import java.io.IOException;
import java.util.List;

public interface ProviderService {

    List<ProviderResponseDto> getAll();

    List<ProviderResponseDto> getByName(String name);

    List<ProviderResponseDto> getByCategory(String category);

    List<ProviderResponseDto> getAccepted();

    ProviderEntity save(String userId, ProviderRequestDto providerRequestDto) throws IOException;

    List<ProviderResponseDto> getByStatus();
    Boolean updateStatus(ProviderUpdateStatusRequestDto providerUpdateStatusRequestDto);
    //ProviderEntity update(ProviderUpdateRequestDto providerUpdateRequestDto) throws IOException;
    ResponseEntity<?> update(ProviderUpdateRequestDto providerUpdateRequestDto);

    List<ProviderResponseDto> getByLocation(double latitude, double longitude);

    List<ProviderResponseDto> getByUser(String username);
}