package media.toloka.rfa.radio.login.service;

import media.toloka.rfa.radio.login.model.Token;
import media.toloka.rfa.radio.login.repo.TokenRepository;
import media.toloka.rfa.security.security.model.Users;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {

    private TokenRepository tokenRepository;

    public void createVerificationToken (Users user, String token) {

    }

    public Token findByToken(String stoken) {
        Optional<Token> token = tokenRepository.findByToken(stoken);
        if (token.isEmpty()) {
            return null;
        } else {
            return token.get();
        }
    }
   public Token findByUser(Users user) {
       Optional<Token> token = tokenRepository.findByUser(user);
       if (token.isEmpty()) {
           return null;
       } else {
           return token.get();
       }
   }
    public void delete(Token myToken) {
        tokenRepository.delete(myToken);
    }
}
