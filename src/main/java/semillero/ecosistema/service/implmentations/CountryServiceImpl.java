package semillero.ecosistema.service.implmentations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import semillero.ecosistema.entity.CountryEntity;
import semillero.ecosistema.exception.CountryExistException;
import semillero.ecosistema.exception.CountryNotExistException;
import semillero.ecosistema.repository.CountryRepository;
import semillero.ecosistema.service.contracts.CountryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    public ResponseEntity<?> getAll(){
        try {
            List<CountryEntity> countrys = countryRepository.findAll();
            return ResponseEntity.ok(countrys);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<?> getByNombre(String name) {
        try {
            CountryEntity country = countryRepository.findByName(name);
            if (country == null) {
                throw new CountryNotExistException();
            }
            System.out.println(country);
            return ResponseEntity.ok(country);
        }catch (CountryNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The Country does not exist with this Name: " + name);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<?> save(CountryEntity newCountry){
        try {
            CountryEntity countryRepeated = countryRepository.findByName(newCountry.getName());
            if(countryRepeated != null){
                throw new CountryExistException();
            }
            countryRepository.save(newCountry);
            return ResponseEntity.status(HttpStatus.CREATED).body("CREATED");
        }catch (CountryExistException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body("COUNTRY ALREADY EXIST");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }

    }

    @Override
    public ResponseEntity<?> update(CountryEntity country){
        try {
            CountryEntity countryRepeated = countryRepository.findById(country.getId())
                    .orElseThrow(CountryNotExistException::new);
            country.setId(countryRepeated.getId());
            countryRepository.save(country);
            return ResponseEntity.ok("UPDATED");
        }catch (CountryNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The Country does not exist with this id: " + country.getId());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
