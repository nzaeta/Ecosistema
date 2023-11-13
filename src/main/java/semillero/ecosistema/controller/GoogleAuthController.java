package semillero.ecosistema.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import semillero.ecosistema.dto.AuthResponse;
import semillero.ecosistema.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/googleLogin")
@RequiredArgsConstructor
public class GoogleAuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody Map<String, String>datos) {
        return ResponseEntity.ok(authService.login(datos));
    }


    @PostMapping("googlelogin")
    public ResponseEntity<AuthResponse> googlelogin(@RequestBody Map<String, String> datos) throws Exception{
       String googleToken = datos.get("credential");

        try {
            return ResponseEntity.ok(authService.googlelogin(googleToken));
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }




}
