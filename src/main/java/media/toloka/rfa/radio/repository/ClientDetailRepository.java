package media.toloka.rfa.radio.repository;

//import media.toloka.rfa.model.Clientdetail;
//import media.toloka.rfa.model.Us;

import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.security.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientDetailRepository extends JpaRepository<Clientdetail, Long> {
//    Clientdetail getClientdetailByUser(Users user);
//    Optional<Clientdetail> findClientdetailByUser(Users user);
//    Clientdetail getByUser(Users user);
    List<Clientdetail> getByUser(Users user);
}
