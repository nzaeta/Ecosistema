package semillero.ecosistema.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import semillero.ecosistema.Dto.ProviderRequestDto;
import semillero.ecosistema.Dto.ProviderResponseDto;
import semillero.ecosistema.Dto.ProviderUpdateRequestDto;
import semillero.ecosistema.Dto.ProviderUpdateStatusRequestDto;
import semillero.ecosistema.exception.*;
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

    @GetMapping("/accepted")
    public ResponseEntity<List<ProviderResponseDto>> getAccepted() {
        List<ProviderResponseDto> providerResponseDtoList = providerService.getAccepted();
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
    public ResponseEntity<?> getByName(@RequestParam String name) {
        List<ProviderResponseDto> providerResponseDtoList = providerService.getByName(name);
        try {
            if(name == null || name.length() < 3) {
                throw new ProviderSearchException();
            }

            if(providerResponseDtoList.size() == 0) {
                return ResponseEntity.status(HttpStatus.OK).body(messageErrorResponse("No hay proveedores con ese nombre"));
            }

            return ResponseEntity.ok(providerResponseDtoList);
        } catch (ProviderSearchException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageErrorResponse(e.getMessage()));

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/get-category")
    public ResponseEntity<?> getByCategory(@RequestParam String categoria) {
        List<ProviderResponseDto> providerResponseDtoList = providerService.getByCategory(categoria);
        try {
            if(providerResponseDtoList.size() == 0) {
                return ResponseEntity.status(HttpStatus.OK).body(messageErrorResponse("No hay proveedores con esa categoría"));
            }

            return ResponseEntity.ok(providerResponseDtoList);
        } catch (ProviderSearchException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageErrorResponse(e.getMessage()));

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    //@Secured("USER")
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam String id, @RequestBody @Valid ProviderRequestDto providerEntity) {

        try {
            if(id == null || providerEntity == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(providerService.save(id, providerEntity));

        } catch (UserNotExistException userNotExistException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageErrorResponse("El usuario ingresado no existe"));

        } catch (ProviderMaxCreatedException providerMaxCreatedException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageErrorResponse("Has superado el límite de 3 proveedores creados por usuario"));

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Listar los proveedores segun su estado
     * ESTADOS -> REVISION_INICIAL, CAMBIOS_REALIZADOS
     */
    @Secured("ADMIN")
    @GetMapping("/get-status")
    public ResponseEntity<List<ProviderResponseDto>> getByStatus() {
        List<ProviderResponseDto> providerResponseDtoList = providerService.getByStatus();
        try {
            if(providerResponseDtoList.size() == 0) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.ok(providerResponseDtoList);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * ESTADOS -> REVISION_INICIAL, ACEPTADO, DENEGADO, REQUIERE_CAMBIOS, CAMBIOS_REALIZADOS
     */
    @Secured("ADMIN")
    @PatchMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestBody  @Valid ProviderUpdateStatusRequestDto providerUpdateStatusRequestDto) {
        try {
            if(providerUpdateStatusRequestDto == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return new ResponseEntity<>(providerService.updateStatus(providerUpdateStatusRequestDto) ? HttpStatus.OK : HttpStatus.NOT_FOUND);

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody @Valid ProviderUpdateRequestDto providerUpdateRequestDto) {

        try {
            if(providerUpdateRequestDto == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(providerService.update(providerUpdateRequestDto));
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