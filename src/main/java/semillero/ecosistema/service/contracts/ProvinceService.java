package semillero.ecosistema.service.contracts;

import org.springframework.http.ResponseEntity;
import semillero.ecosistema.entity.ProvinceEntity;

import java.util.List;

public interface ProvinceService {
    ResponseEntity<?> getAll();
    ResponseEntity<?> getByName(String name);
    ResponseEntity<?> getByCountryId(String country_id);
    ResponseEntity<?> save(ProvinceEntity provinceEntity);
    ResponseEntity<?> update( ProvinceEntity provinceEntity);


}
