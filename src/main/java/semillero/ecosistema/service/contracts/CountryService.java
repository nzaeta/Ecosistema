package semillero.ecosistema.service.contracts;

import org.springframework.http.ResponseEntity;
import semillero.ecosistema.entity.CountryEntity;

import java.util.List;

public interface CountryService {
    ResponseEntity<?> getAll();
    ResponseEntity<?> getByNombre(String nombre);
    ResponseEntity<?> save(CountryEntity countryEntity);
    ResponseEntity<?> update(CountryEntity countryEntity);
}
