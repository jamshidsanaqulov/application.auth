package project.auth.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.auth.application.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,String> {
     Optional<Role> findById(String s);
}
