package media.toloka.rfa.radio.repository;


import media.toloka.rfa.radio.model.Token;
import media.toloka.rfa.security.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

//    Token findTokenByUser(User user);

    Optional<Token> findByToken(String token);

    Optional<Token> findByUser(Users user);

    void deleteById(Long id);

}