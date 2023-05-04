package project.auth.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.auth.application.entity.User;
import project.auth.application.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean existByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public List<User> findAll(String name){
        return userRepository.findByName(name);
    }
}