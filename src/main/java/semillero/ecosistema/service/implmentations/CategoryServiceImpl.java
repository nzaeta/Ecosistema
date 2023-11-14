package semillero.ecosistema.service.implmentations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import semillero.ecosistema.entity.CategoryEntity;
import semillero.ecosistema.exception.CategoryExistException;
import semillero.ecosistema.exception.CategoryNotExistException;
import semillero.ecosistema.repository.CategoryRepository;
import semillero.ecosistema.service.contracts.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryEntity> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryEntity getByName(String name) {
        return categoryRepository.findByNombre(name);
    }

    @Override
    public CategoryEntity save(CategoryEntity categoryEntity) {
        CategoryEntity categoryRepeated = getByName(categoryEntity.getNombre());
        if(categoryRepeated != null) {
            throw new CategoryExistException();
        }
        return categoryRepository.save(categoryEntity);
    }

    @Override
    public CategoryEntity update(String nameCategory, CategoryEntity categoryEntity) {
        CategoryEntity categoryRepeated = getByName(nameCategory);
        if(categoryRepeated == null) {
            throw new CategoryNotExistException();
        }
        categoryEntity.setId(categoryRepeated.getId());
        return categoryRepository.save(categoryEntity);
    }
}