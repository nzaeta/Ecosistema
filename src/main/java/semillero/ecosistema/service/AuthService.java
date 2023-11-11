package semillero.ecosistema.service;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import semillero.ecosistema.dto.AuthResponse;
import semillero.ecosistema.entity.UserEntity;
import semillero.ecosistema.jwt.JwtService;
import semillero.ecosistema.repository.UserRepository;


import java.io.IOException;
import java.util.Collections;


@Service
@RequiredArgsConstructor
public class AuthService {


    @Value("${google.client-id}")
    String CLIENT_ID;

//    private final UserRepository userRepository;
//    private final JwtService jwtService;
//    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;

   public AuthResponse login(String googleToken) throws IOException {



       // VALIDADOR TOKEN DE GOOGLE
//       final NetHttpTransport transport = new NetHttpTransport();
//       final GsonFactory gsonFactory = GsonFactory.getDefaultInstance();
//       GoogleIdTokenVerifier.Builder verifier =
//               new GoogleIdTokenVerifier.Builder(transport, gsonFactory)
//                       .setAudience(Collections.singletonList(CLIENT_ID));
//
//       final GoogleIdToken tokenVerificado = GoogleIdToken.parse(verifier.getJsonFactory(), googleToken);
//       final GoogleIdToken.Payload payload = tokenVerificado.getPayload();


       // BUSCAR EL USUARIO EN LA BD Y TRAER LOS DATOS. SI NO EXISTE HAY QUE GUARDARLO

//       UserEntity usuario = new UserEntity();
//       if (userRepository.findByEmail(payload.getEmail()).get().isPresent) {
//           usuario = userRepository.findByEmail(payload.getEmail()).get();
//       }else{
//           usuario = saveUsuario(payload.getEmail(), tokenDto);
//       }



            // GENERO EL JWT

//           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), null));
//           UserDetails user=userRepository.findByEmail(email).orElseThrow();
//           String token=jwtService.getToken(user);
//           return AuthResponse.builder()
//                   .token(token)
//                   .build();
            return null;
       }

//    public AuthResponse register(String email) {
//        User user = User.builder()
//                .username(request.getUsername())
//                .password(passwordEncoder.encode( request.getPassword()))
//                .firstname(request.getFirstname())
//                .lastname(request.lastname)
//                .country(request.getCountry())
//                .role(Role.USER)
//                .build();
//
//        userRepository.save(user);
//
//        return AuthResponse.builder()
//                .token(jwtService.getToken(user))
//                .build();

 //   }

}