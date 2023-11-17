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
    public ResponseEntity<List<ProvinceEntity>> getAll(){
        List<ProvinceEntity> provinceEntityList = provinceService.getAll();
        try {
            if (provinceEntityList.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(provinceEntityList);
        }catch (ResponseStatusException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/get-name")
    public ResponseEntity<ProvinceEntity> getByName(@RequestParam String name){
        ProvinceEntity provinceEntity = provinceService.getByNombre(name);
        try {
            if(provinceEntity == null){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(provinceEntity);
        }catch (ResponseStatusException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/by-pais")
    private ResponseEntity<List<ProvinceEntity>> getByPais(@RequestParam int pais_id){
        List<ProvinceEntity> provinceEntityList = provinceService.getByPais(pais_id);
        try {
            if (provinceEntityList.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(provinceEntityList);
        } catch (ResponseStatusException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ProvinceEntity provinceEntity){
        try {
            return new ResponseEntity<>(provinceService.save(provinceEntity),HttpStatus.CREATED);
        }catch (ProvinceExistException provinceExistException){
            String errorMessage = "La provincia ya existe";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestParam String nameProvince, @RequestBody ProvinceEntity provinceEntity){
        try {
            return  new ResponseEntity<>(provinceService.update(nameProvince,provinceEntity),HttpStatus.OK);
        }catch (ProvinceNotExistException provinceNotExistException){
            String errorMessage = "La provincia no existe";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }catch (ResponseStatusException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
