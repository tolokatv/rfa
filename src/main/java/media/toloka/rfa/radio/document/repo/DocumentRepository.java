package media.toloka.rfa.radio.document.repo;




import media.toloka.rfa.model.Clientdetail;
import media.toloka.rfa.model.Documents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Documents, Long> {

    List<Documents> findByClientdetail(Clientdetail clientdetail);

    Documents findDocumentById(Long id);

}
