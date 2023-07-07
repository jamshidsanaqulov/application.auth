package project.auth.application.resourse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.auth.application.config.SwaggerConfig;
import project.auth.application.entity.user.User;
import project.auth.application.entity.user.UserPayload;
import project.auth.application.resourse.vm.LoginVM;
import project.auth.application.resourse.vm.UserDto;
import project.auth.application.security.JwtTokenProvider;
import project.auth.application.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@SecurityRequirement(name = SwaggerConfig.BEARER)
public class UserJWTResource {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public UserJWTResource(UserService userService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
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

    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody UserPayload payload) {
        User user = userService.create(payload);
        return ResponseEntity.ok(UserDto.minResponse(user));
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
