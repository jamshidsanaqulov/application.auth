package project.auth.application.resourse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.auth.application.entity.User;
import project.auth.application.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class UserResource {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody User user) {
        if (userService.existByLogin(user.getLogin())) {
            return new ResponseEntity("Bu login mavjud", HttpStatus.BAD_REQUEST);
        }
        if (checkPasswordLength(user.getPassword())) {
            return new ResponseEntity("Password uzunligi 4 dan kam", HttpStatus.BAD_REQUEST);
        }
        User result = userService.save(user);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-all")
    public ResponseEntity getAll() {
        List<User> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    public ResponseEntity getAll(@RequestParam String name) {
        List<User> users = userService.findAll(name);
        return ResponseEntity.ok(users);
    }

    private boolean checkPasswordLength(String password) {
        return password.length() <= 4;
    }
}
