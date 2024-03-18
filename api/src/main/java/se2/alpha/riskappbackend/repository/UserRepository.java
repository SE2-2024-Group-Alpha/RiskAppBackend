package se2.alpha.riskappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se2.alpha.riskappbackend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
