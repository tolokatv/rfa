package media.toloka.rfa.security.repository;

import media.toloka.rfa.security.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSecurityRepository extends JpaRepository<Users, Long> {
    Optional<Users> findUserByEmail(String email);
}
