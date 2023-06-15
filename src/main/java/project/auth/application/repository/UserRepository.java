package project.auth.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.auth.application.entity.user.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByLogin(String login);

    User findByLogin(String login);

    @Query("select u from User u where upper( u.firstName) like upper(concat('%', :name, '%'))")
    List<User> findByName(@Param("name") String name);
}
