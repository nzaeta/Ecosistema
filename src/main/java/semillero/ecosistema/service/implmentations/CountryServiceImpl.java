package semillero.ecosistema.service.implmentations;

import lombok.RequiredArgsConstructor;
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
    public List<CountryEntity> getAll(){
        return countryRepository.findAll();
    }
    @Override
    public CountryEntity getByNombre(String nombre){
        return countryRepository.findByNombre(nombre);
    }

    @Override
    public CountryEntity save(CountryEntity countryEntity){
        CountryEntity countryRepeated = getByNombre(countryEntity.getNombre());
        if(countryRepeated != null){
            throw new CountryExistException();
        }
        return countryRepository.save(countryEntity);
    }

    @Override
    public CountryEntity update(String nameCountry, CountryEntity countryEntity){
        CountryEntity countryRepeated = getByNombre(nameCountry);
        if (countryRepeated == null){
            throw new CountryNotExistException();
        }
        countryEntity.setId(countryRepeated.getId());
        return countryRepository.save(countryEntity);
    }

}
