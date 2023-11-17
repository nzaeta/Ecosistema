package semillero.ecosistema.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import semillero.ecosistema.entity.ProviderEntity;
import semillero.ecosistema.exception.ErrorResponse;
import semillero.ecosistema.exception.ProviderMaxCreatedException;
import semillero.ecosistema.exception.ProviderNotExistException;
import semillero.ecosistema.exception.UserNotExistException;
import semillero.ecosistema.service.contracts.ProviderService;

@RestController
@RequestMapping(path = "/provider")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam Long userId, @RequestBody ProviderEntity providerEntity) {
        System.out.println("Controller ");
        /*if(providerEntity == null) {
            System.out.println("Controller NULL");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }*/
        System.out.println("Controller CREADO");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(providerService.save(userId, providerEntity));
        /*
        try {
            System.out.println("Controller ");
            return ResponseEntity.status(HttpStatus.CREATED)
                            .body(providerService.save(userId, providerEntity));

        } catch (ProviderMaxCreatedException providerMaxCreatedException) {
            String errorMessage = "Has superado el limite de 3 proveedores creados por usuario";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        } catch (UserNotExistException userNotExistException) {
            String errorMessage = "El usuario ingresado no existe";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        } catch (ResponseStatusException e) {
            System.out.println("BAD REQUEST ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        */
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long userId, @RequestBody ProviderEntity providerEntity) {

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(providerService.update(userId,providerEntity));
        } catch (ProviderNotExistException providerNotExistException) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}