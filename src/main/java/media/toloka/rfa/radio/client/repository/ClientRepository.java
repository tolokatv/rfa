package media.toloka.rfa.radio.client.repository;

import media.toloka.rfa.security.security.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Users, Long> {
    Long saveUser(Users user);

    Optional<Users> findUserByEmail(String email);
}
