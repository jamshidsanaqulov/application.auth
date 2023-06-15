package project.auth.application.resourse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.auth.application.config.SwaggerConfig;
import project.auth.application.entity.user.User;
import project.auth.application.entity.user.UserPayload;
import project.auth.application.resourse.vm.UserDto;
import project.auth.application.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@SecurityRequirement(name = SwaggerConfig.BEARER)
public class UserResource {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody UserPayload payload) {
        if (userService.existByLogin(payload.getLogin())) {
            return new ResponseEntity<>("Bu login mavjud", HttpStatus.BAD_REQUEST);
        }
        if (checkPasswordLength(payload.getPassword())) {
            return new ResponseEntity<>("Password uzunligi 4 dan kam", HttpStatus.BAD_REQUEST);
        }

        User user = new User();

        user.setLogin(payload.getLogin());
        user.setPassword(payload.getPassword());
        user.setFirstName(payload.getFirstName());
        user.setLastName(payload.getLastName());
        user.setBirthday(payload.getBirthday());

        user = userService.save(user);

        return ResponseEntity.ok(UserDto.minResponse(user));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<UserDto>> getAll() {
        List<User> users = userService.getAll();
        List<UserDto> dtos = users.stream()
                .map(user -> {
                    UserDto userDto = new UserDto();
                    userDto.setLogin((user.getLogin()));
                    userDto.setFirstName(user.getFirstName());
                    userDto.setLastName(user.getLastName());
                    userDto.setBirthday(user.getBirthday());
                    return userDto;
                })
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> getAll(@RequestParam String name) {
        List<User> users = userService.findAll(name);
        return ResponseEntity.ok(users);
    }

    private boolean checkPasswordLength(String password) {
        return password.length() <= 4;
    }
}
