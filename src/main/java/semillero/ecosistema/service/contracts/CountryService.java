package semillero.ecosistema.service.contracts;

import semillero.ecosistema.entity.CountryEntity;

import java.util.List;

public interface CountryService {
    List<CountryEntity> getAll();
    CountryEntity getByNombre(String nombre);
    CountryEntity save(CountryEntity countryEntity);
    CountryEntity update(String nameCountry, CountryEntity countryEntity);
}
