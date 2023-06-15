package project.auth.application.resourse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.auth.application.config.SwaggerConfig;
import project.auth.application.resourse.vm.LoginVM;
import project.auth.application.security.JwtTokenProvider;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@SecurityRequirement(name = SwaggerConfig.BEARER)
public class UserJWTResource {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public UserJWTResource(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/login")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM){
        var authenticationToken= new UsernamePasswordAuthenticationToken(
                loginVM.getLogin(),
                loginVM.getPassword()
        );
        var authenticate = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        var jwt = jwtTokenProvider.createToken(loginVM.getLogin(),authenticate);
        var headers =new HttpHeaders();
        headers.add("Authorization","Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt),headers, HttpStatus.OK);
    }
    static class JWTToken{
        private String idToken;

        public JWTToken(String idToken) {
            this.idToken = idToken;
        }

        public String getIdToken() {
            return idToken;
        }

        public void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
