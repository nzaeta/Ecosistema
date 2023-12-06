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
    ResponseEntity<?> getAll(){
        return countryService.getAll();
    }
    @GetMapping("/get-name")
    ResponseEntity<?> getByName(@RequestParam String name){
        return countryService.getByNombre(name);
    }
    @PostMapping("/save")
    ResponseEntity<?> save(@RequestBody CountryEntity countryEntity){
        return countryService.save(countryEntity);
    }
    @PatchMapping("/update")
    ResponseEntity<?> update(@RequestBody CountryEntity countryEntity){
        return countryService.update(countryEntity);
    }
}
