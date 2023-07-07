package project.auth.application.resourse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.auth.application.config.SwaggerConfig;
import project.auth.application.entity.user.User;
import project.auth.application.entity.user.UserPayload;
import project.auth.application.resourse.vm.UserDto;
import project.auth.application.service.UserService;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = SwaggerConfig.BEARER)
public class UserResource {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto>getOne(@PathVariable Long id){
        User user = userService.findById(id);
        return ResponseEntity.ok(UserDto.minResponse(user));
    }
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> getAll(@RequestParam String name) {
        List<User> users = userService.findAll(name);
        List<UserDto> dtos = users.stream()
                .map(user -> {
                    UserDto userDto = new UserDto();
                    userDto.setId(user.getId());
                    userDto.setLogin((user.getLogin()));
                    userDto.setFirstName(user.getFirstName());
                    userDto.setLastName(user.getLastName());
                    userDto.setBirthday(user.getBirthday());
                    return userDto;
                })
                .toList();
        return ResponseEntity.ok(dtos);
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<UserDto>> getAll() {
        List<User> users = userService.getAll();
        List<UserDto> dtos = users.stream()
                .map(user -> {
                    UserDto userDto = new UserDto();
                    userDto.setId(user.getId());
                    userDto.setLogin((user.getLogin()));
                    userDto.setFirstName(user.getFirstName());
                    userDto.setLastName(user.getLastName());
                    userDto.setBirthday(user.getBirthday());
                    return userDto;
                })
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody UserPayload payload
    ) {
        User user = userService.update(id, payload);
        return ResponseEntity.ok(UserDto.minResponse(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.ok("Deleted");
    }

}
