package semillero.ecosistema.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import semillero.ecosistema.entity.CategoryEntity;
import semillero.ecosistema.exception.CategoryExistException;
import semillero.ecosistema.exception.CategoryNotExistException;
import semillero.ecosistema.exception.ErrorResponse;
import semillero.ecosistema.service.contracts.CategoryService;

import java.util.List;

@RestController
@RequestMapping(path = "/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<List<CategoryEntity>> getAll() {
        List<CategoryEntity> categoryEntityList = categoryService.getAll();
        try {
            if (categoryEntityList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(categoryEntityList);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/get-name")
    public ResponseEntity<CategoryEntity> getByName(@RequestParam String name) {
        CategoryEntity categoryEntity = categoryService.getByName(name);
        try {
            if (categoryEntity == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(categoryEntity);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody CategoryEntity categoryEntity) {

        try {
            return new ResponseEntity(categoryService.save(categoryEntity), HttpStatus.CREATED);
        } catch (CategoryExistException categoryExistException) {
            String errorMessage = "La categoría ingresada ya existe";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestParam String nameCategory, @RequestBody CategoryEntity categoryEntity) {
        try {
            return new ResponseEntity(categoryService.update(nameCategory, categoryEntity), HttpStatus.OK);
        } catch (CategoryNotExistException categoryNotExistException) {
            String errorMessage = "La categoría ingresada no existe";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}