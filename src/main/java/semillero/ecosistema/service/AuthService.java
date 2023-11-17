package semillero.ecosistema.service;

//import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;
import semillero.ecosistema.dto.AuthResponse;
//import semillero.ecosistema.entity.UserEntity;
import semillero.ecosistema.entity.UserEntity;
import semillero.ecosistema.jwt.JwtService;
import semillero.ecosistema.repository.UserRepository;


import java.io.IOException;
import java.util.Collections;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String CLIENT_ID;

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public AuthResponse login(Map<String, String> datos) {
        String email = datos.get("email");
        UserEntity user = userRepository.findByEmail(email);

        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }


    public AuthResponse googlelogin(String googleToken) throws IOException {

        // VALIDADOR TOKEN DE GOOGLE
        final NetHttpTransport transport = new NetHttpTransport();
        final GsonFactory gsonFactory = GsonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier =
                new GoogleIdTokenVerifier.Builder(transport, gsonFactory)
                        .setAudience(Collections.singletonList(CLIENT_ID));

        final GoogleIdToken tokenVerificado = GoogleIdToken.parse(verifier.getJsonFactory(), googleToken);
        final GoogleIdToken.Payload payload = tokenVerificado.getPayload();

        String email = payload.getEmail();
        String familyName = (String) payload.get("family_name");
        String givenName = (String) payload.get("given_name");
        String pictureUrl = (String) payload.get("picture");

        // BUSCAR EL USUARIO EN LA BD Y TRAER LOS DATOS. SI NO EXISTE HAY QUE GUARDARLO

       UserEntity usuario = new UserEntity();

       if (userRepository.findByEmail(email) != null) {
           usuario = userRepository.findByEmail(payload.getEmail());
           usuario.setApellido(familyName);
           usuario.setNombre(givenName);
           usuario.setImagen(pictureUrl);
           usuario = userRepository.save(usuario);
       }else{
           usuario.setApellido(familyName);
           usuario.setDeleted(false);
           usuario.setEmail(email);
           usuario.setNombre(givenName);
           usuario.setRol("USER");
           usuario.setImagen(pictureUrl);

           usuario = userRepository.save(usuario);
       }

        //GENERA TOKEN
        String token = jwtService.getToken(usuario);
        return AuthResponse.builder()
                .token(token)
                .build();

    }

}