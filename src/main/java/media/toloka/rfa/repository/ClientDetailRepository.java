package media.toloka.rfa.repository;



import media.toloka.rfa.model.Clientdetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientDetailRepository extends JpaRepository<Clientdetail, Long> {
//    Clientdetail getClientdetailByUser(Users user);
//    Optional<Clientdetail> findClientdetailByUser(Users user);
//    Clientdetail getByUser(Users user);
    List<Clientdetail> getByUser(Long user);
    Clientdetail getById(Long id);

}
