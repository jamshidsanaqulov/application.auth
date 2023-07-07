package project.auth.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import project.auth.application.entity.Role;
import project.auth.application.entity.user.User;
import project.auth.application.entity.user.UserPayload;
import project.auth.application.repository.RoleRepository;
import project.auth.application.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public User update(Long id, UserPayload payload) {
        var user = findById(id);
        if (!user.getLogin().equals(payload.getLogin())) {
            if (existByLogin(payload.getLogin())) {
                throw new IllegalArgumentException("This username already exist");
            }
            user.setLogin(payload.getLogin());
        }
        if (StringUtils.hasText(payload.getPassword())) {
            user.setPassword(passwordEncoder.encode(payload.getPassword()));
        }
        user.setLastName(payload.getLastName());
        user.setBirthday(payload.getBirthday());
        user.setFirstName(payload.getFirstName());
        return user;
    }

    public User save(User user) {
        return userRepository.save(user);
    }



    @Transactional(readOnly = true)
    public User findById(Long id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new IllegalArgumentException("USER NOT FOUND");
    }

    @Transactional
    public User create(UserPayload payload) {
        if (existByLogin(payload.getLogin())) {
            throw new IllegalStateException("Bu login mavjud");
        }
        if (payload.getPassword().length() <= 4) {
            throw new IllegalStateException("Password uzunligi 4 dan kam");
        }
        User user = new User();
        user.setLogin(payload.getLogin());
        user.setPassword(payload.getPassword());
        user.setFirstName(payload.getFirstName());
        user.setLastName(payload.getLastName());
        user.setBirthday(payload.getBirthday());
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findById("ROLE_USER").get());
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(payload.getPassword()));
        return save(user);
    }

    public boolean existByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public List<User> findAll(String name) {
        return userRepository.findByName(name);
    }

    public void delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}