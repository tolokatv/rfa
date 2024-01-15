package media.toloka.rfa.radio.document.service;

import media.toloka.rfa.radio.document.model.IDDocuments;
import media.toloka.rfa.radio.document.repo.DocumentRepository;
import media.toloka.rfa.security.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public IDDocuments GetDocument(Long id) {
        return documentRepository.findDocumentById(id);
    }

    public void saveDocument(IDDocuments document) {
        documentRepository.save(document);
    }

    public List<IDDocuments> listDocumentsByUser(Users user) {
        return documentRepository.findDocumentByUser(user);
    }

    public void saveDocumentInfo(Path destination) {
    }
}
