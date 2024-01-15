package media.toloka.rfa.radio.document.repo;




import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.document.model.Documents;
import media.toloka.rfa.security.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Documents, Long> {

    List<Documents> findDocumentByClientdetail(Clientdetail clientdetail);

    Documents findDocumentById(Long id);

}
