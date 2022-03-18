package crypto_simulator.simulator.repository;

import crypto_simulator.simulator.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// original : https://github.com/SilverNine/spring-boot-jwt-tutorial
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);

}