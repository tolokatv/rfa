package media.toloka.rfa.radio.document.repo;


import media.toloka.rfa.radio.document.model.IDDocuments;
import media.toloka.rfa.security.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<IDDocuments, Long> {

    List<IDDocuments> findDocumentByUser(Users user);

    IDDocuments findDocumentById(Long id);

}
