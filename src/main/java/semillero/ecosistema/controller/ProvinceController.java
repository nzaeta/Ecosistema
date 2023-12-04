package semillero.ecosistema.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import semillero.ecosistema.entity.ProvinceEntity;
import semillero.ecosistema.exception.ErrorResponse;
import semillero.ecosistema.exception.ProvinceExistException;
import semillero.ecosistema.exception.ProvinceNotExistException;
import semillero.ecosistema.service.contracts.ProvinceService;

import java.util.List;

@RestController
@RequestMapping(path = "/provinces")
@RequiredArgsConstructor
public class ProvinceController {
    private final ProvinceService provinceService;

    @GetMapping("/all")
    ResponseEntity<?> getAll(){
        return provinceService.getAll();
    }

    @GetMapping("/get-name")
    ResponseEntity<?> getByName(@RequestParam String name){
        return provinceService.getByName(name);
    }

    @GetMapping("/by-country")
    ResponseEntity<?> getByCountryId(@RequestParam Long country_id){
        return provinceService.getByCountryId(country_id);
    }

    @PostMapping("/save")
    ResponseEntity<?> save(@RequestBody ProvinceEntity provinceEntity){
        return provinceService.save(provinceEntity);
    }

    @PatchMapping("/update")
    ResponseEntity<?> update(@RequestBody ProvinceEntity provinceEntity){
        return provinceService.update(provinceEntity);
    }
}
