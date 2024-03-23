package se2.alpha.riskappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se2.alpha.riskappbackend.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
