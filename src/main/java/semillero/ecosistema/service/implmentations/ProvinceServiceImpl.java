package semillero.ecosistema.service.implmentations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import semillero.ecosistema.entity.CountryEntity;
import semillero.ecosistema.entity.ProvinceEntity;
import semillero.ecosistema.exception.CountryNotExistException;
import semillero.ecosistema.exception.ProvinceExistException;
import semillero.ecosistema.exception.ProvinceNotExistException;
import semillero.ecosistema.repository.CountryRepository;
import semillero.ecosistema.repository.ProvinceRepository;
import semillero.ecosistema.service.contracts.ProvinceService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceServiceImpl implements ProvinceService {
    private final ProvinceRepository provinceRepository;
    private final CountryRepository countryRepository;

    @Override
    public ResponseEntity<?> getAll(){
        try {
            List<ProvinceEntity> provinces = provinceRepository.findAll();
            return ResponseEntity.ok(provinces);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getByName(String name){
        try {
            ProvinceEntity province = provinceRepository.findByName(name);
            if (province == null){
                throw new ProvinceNotExistException();
            }
            return ResponseEntity.ok(province);
        }catch (ProvinceNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @Override
    public ResponseEntity<?> getByCountryId(Long country_id){
        try {
            countryRepository.findById(country_id).orElseThrow(CountryNotExistException::new);
            List<ProvinceEntity> provinces = provinceRepository.findByCountryId(country_id);
            if (provinces.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("This country doesnt have Provinces");
            }
            return ResponseEntity.ok(provinces);
        }catch (CountryNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> save(ProvinceEntity provinceEntity){
        try {
            ProvinceEntity provinceRepeated = provinceRepository.findByName(provinceEntity.getName());
            if(provinceRepeated != null){
                throw new ProvinceExistException();
            }
            provinceRepository.save(provinceEntity);
            return ResponseEntity.ok(HttpStatus.CREATED);
        }catch (ProvinceExistException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> update(ProvinceEntity provinceEntity){
        try {
            ProvinceEntity provinceRepeated = provinceRepository.findByName(provinceEntity.getName());
            if(provinceRepeated == null){
                throw new ProvinceNotExistException();
            }
            provinceEntity.setId(provinceRepeated.getId());
            provinceRepository.save(provinceEntity);
            return ResponseEntity.ok().body("UPDATED");
        }catch (ProvinceNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }


}
