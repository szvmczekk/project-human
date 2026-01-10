package pl.szvmczek.projecthuman.domain.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    @EntityGraph("User.roles")
    Optional<User> findByEmail(String email);
}
