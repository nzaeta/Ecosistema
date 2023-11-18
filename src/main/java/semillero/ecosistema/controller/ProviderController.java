package semillero.ecosistema.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import semillero.ecosistema.Dto.ProviderResponseDto;
import semillero.ecosistema.entity.ProviderEntity;
import semillero.ecosistema.exception.ErrorResponse;
import semillero.ecosistema.exception.ProviderMaxCreatedException;
import semillero.ecosistema.exception.ProviderNotExistException;
import semillero.ecosistema.exception.UserNotExistException;
import semillero.ecosistema.service.contracts.ProviderService;

import java.util.List;

@RestController
@RequestMapping(path = "/provider")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;


    @GetMapping("/all")
    public ResponseEntity<List<ProviderResponseDto>> getAll() {
        List<ProviderResponseDto> providerResponseDtoList = providerService.getAll();
        try {
            if(providerResponseDtoList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(providerResponseDtoList);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/get-name")
    public ResponseEntity<List<ProviderResponseDto>> getByName(@RequestParam String name) {
        List<ProviderResponseDto> providerResponseDtoList = providerService.getByName(name);
        try {
            if(name == null || providerResponseDtoList.size() == 0) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.ok(providerResponseDtoList);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam Long id, @RequestBody ProviderEntity providerEntity) {

        try {
            if(id == null || providerEntity == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(providerService.save(id, providerEntity));

        } catch (UserNotExistException userNotExistException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageErrorResponse("El usuario ingresado no existe"));

        } catch (ProviderMaxCreatedException providerMaxCreatedException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageErrorResponse("Has superado el limite de 3 proveedores creados por usuario"));

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long id, @RequestBody ProviderEntity providerEntity) {

        try {
            if(id == null || providerEntity == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(providerService.update(id,providerEntity));
        } catch (UserNotExistException userNotExistException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageErrorResponse("El usuario ingresado no existe"));
        } catch (ProviderNotExistException providerNotExistException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageErrorResponse("El proveedor no existe"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private ErrorResponse messageErrorResponse(String message) {
        return new ErrorResponse(message);
    }
}