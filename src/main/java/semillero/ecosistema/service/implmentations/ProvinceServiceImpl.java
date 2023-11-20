package semillero.ecosistema.service.implmentations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import semillero.ecosistema.entity.ProvinceEntity;
import semillero.ecosistema.exception.CountryNotExistException;
import semillero.ecosistema.exception.ProvinceExistException;
import semillero.ecosistema.repository.ProvinceRepository;
import semillero.ecosistema.service.contracts.ProvinceService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceServiceImpl implements ProvinceService {
    private final ProvinceRepository provinceRepository;

    @Override
    public List<ProvinceEntity> getAll(){
        return provinceRepository.findAll();
    }

    @Override
    public ProvinceEntity getByNombre(String nombre){
        return provinceRepository.findByNombre(nombre);
    }

    @Override
    public List<ProvinceEntity> getByPais(Long pais_id){
        return provinceRepository.findByPaisId(pais_id);
    }

    @Override
    public ProvinceEntity save(ProvinceEntity provinceEntity){
        ProvinceEntity provinceRepeated = getByNombre(provinceEntity.getNombre());
        if(provinceRepeated != null){
            throw new ProvinceExistException();
        }
        return provinceRepository.save(provinceEntity);
    }

    @Override
    public ProvinceEntity update(String nameProvince, ProvinceEntity provinceEntity){
        ProvinceEntity provinceRepeated = getByNombre(nameProvince);
        if(provinceRepeated == null){
            throw new CountryNotExistException();
        }
        provinceEntity.setId(provinceRepeated.getId());
        return provinceRepository.save(provinceEntity);
    }


}
