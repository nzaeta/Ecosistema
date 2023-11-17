package semillero.ecosistema.service.contracts;

import semillero.ecosistema.entity.ProviderEntity;

import java.util.Optional;

public interface ProviderService {

    ProviderEntity save(Long userId, ProviderEntity providerEntity);

    ProviderEntity update(Long userId,ProviderEntity providerEntity);

    Optional<ProviderEntity> updateByUserId(ProviderEntity providerEntity);
}