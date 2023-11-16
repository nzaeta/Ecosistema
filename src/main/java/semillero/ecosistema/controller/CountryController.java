package semillero.ecosistema.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import semillero.ecosistema.entity.CountryEntity;
import semillero.ecosistema.exception.CountryExistException;
import semillero.ecosistema.exception.CountryNotExistException;
import semillero.ecosistema.exception.ErrorResponse;
import semillero.ecosistema.service.contracts.CountryService;

import java.util.List;

@RestController
@RequestMapping(path = "/countrys")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;

    @GetMapping("/all")
    public ResponseEntity<List<CountryEntity>> getAll(){
        List<CountryEntity> countryEntityList = countryService.getAll();
        try {
            if(countryEntityList.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(countryEntityList);
        }catch (ResponseStatusException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/get-name")
    public ResponseEntity<CountryEntity> getByName(@RequestParam String name){
        CountryEntity countryEntity = countryService.getByNombre(name);
        try {
            if (countryEntity == null){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(countryEntity);
        }catch (ResponseStatusException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody CountryEntity countryEntity){
        try {
            return new ResponseEntity(countryService.save(countryEntity),HttpStatus.CREATED);
        }catch (CountryExistException countryExistException){
            String errorMessage = "El pais ingresado ya existe";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }catch (ResponseStatusException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestParam String nameCountry, @RequestBody CountryEntity countryEntity){
        try {
            return new ResponseEntity(countryService.update(nameCountry,countryEntity),HttpStatus.OK);
        }catch (CountryNotExistException countryNotExistException){
            String errorMessage = "El pais no existe";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }catch (ResponseStatusException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
