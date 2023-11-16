package semillero.ecosistema.service.contracts;

import semillero.ecosistema.entity.CategoryEntity;

import java.util.List;

public interface CategoryService {

    List<CategoryEntity> getAll();
    CategoryEntity getByName(String name);
    CategoryEntity save(CategoryEntity categoryEntity);
    CategoryEntity update(String nameCategory, CategoryEntity categoryEntity);
}