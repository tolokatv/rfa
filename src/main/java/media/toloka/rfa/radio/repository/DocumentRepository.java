package media.toloka.rfa.radio.repository;




import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Documents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DocumentRepository extends JpaRepository<Documents, Long> {

    List<Documents> findByClientdetail(Clientdetail Clientdetailrfa);

    Documents getById(Long id);

}
