package media.toloka.rfa.repository;

//import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.security.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
//    Long saveUser(Users user);

//    Optional<Users> getByEmail(String email);
//    Users getUserByEmail(String email);
//    Clientdetail getByUser(Users user);
//    void save(Users user);

    Users getUserByEmail(String email);
}
