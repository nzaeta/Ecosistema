package semillero.ecosistema.service.contracts;

import semillero.ecosistema.entity.ProvinceEntity;

import java.util.List;

public interface ProvinceService {
    List<ProvinceEntity> getAll();
    ProvinceEntity getByNombre(String nombre);
    List<ProvinceEntity> getByPais(int pais_id);
    ProvinceEntity save(ProvinceEntity provinceEntity);
    ProvinceEntity update(String nameProvince, ProvinceEntity provinceEntity);

}
