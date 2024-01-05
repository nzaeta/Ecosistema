package semillero.ecosistema.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import semillero.ecosistema.dto.*;
import semillero.ecosistema.exception.*;
import semillero.ecosistema.service.contracts.ProviderService;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/provider")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @Secured("ADMIN")
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        List<ProviderResponseDto> providerResponseDtoList = providerService.getAll();
        try {
            if(providerResponseDtoList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            Map<String, Object> response = new HashMap<>();
            List<ProviderResponseDto> nuevos = new ArrayList();
            List<ProviderResponseDto> aceptados = new ArrayList();
            List<ProviderResponseDto> revision = new ArrayList();
            List<ProviderResponseDto> denegados = new ArrayList();

            for (ProviderResponseDto prov : providerResponseDtoList) {
                if (prov.getStatus().equalsIgnoreCase("REVISION_INICIAL")) {
                    nuevos.add(prov);
                } else if (prov.getStatus().equalsIgnoreCase("ACEPTADO")) {
                    aceptados.add(prov);
                } else if (prov.getStatus().equalsIgnoreCase("DENEGADO")){
                    denegados.add(prov);
                } else {
                    revision.add(prov);
                }
            }
            response.put("nuevos", nuevos);
            response.put("aceptados", aceptados);
            response.put("revision", revision);
            response.put("denegados", denegados);

            return ResponseEntity.ok(response);
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

    @GetMapping("/get-location")
    public ResponseEntity<?> getByLocation(@RequestParam double latitude, @RequestParam double longitude) {

        try {
            List<ProviderResponseDto> providerResponseDtoList = providerService.getByLocation(latitude, longitude);

            if(providerResponseDtoList.size() == 0) {
                return ResponseEntity.status(HttpStatus.OK).body(messageErrorResponse("No hay proveedores en tu ciudad"));
            }

            return ResponseEntity.ok(providerResponseDtoList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageErrorResponse(e.getMessage()));
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Secured("USER")
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam String id, @ModelAttribute @Valid ProviderRequestDto providerEntity) {

        try {
            if(id == null || providerEntity == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(providerService.save(id, providerEntity));

        } catch (UserNotExistException userNotExistException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageErrorResponse("El usuario ingresado no existe"));

        } catch (ProviderMaxCreatedException providerMaxCreatedException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageErrorResponse("Has superado el límite de 3 proveedores creados por usuario"));

        } catch (ResponseStatusException | IOException e) {
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
    @Secured("USER")
    @PutMapping("/update")
    public ResponseEntity<?> update(@ModelAttribute @Valid ProviderUpdateRequestDto providerUpdateRequestDto) {

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ErrorResponse messageErrorResponse(String message) {
        return new ErrorResponse(message);
    }

    @Secured("USER")
    @GetMapping("/get-user")
    public ResponseEntity<List<ProviderResponseDto>> getByUser(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        List<ProviderResponseDto> providers = providerService.getByUser(username);
        try {
            if(providers.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

            return ResponseEntity.status(HttpStatus.OK).body(providers);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}