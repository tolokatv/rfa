package media.toloka.rfa.radio.document.service;

import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.document.model.Documents;
import media.toloka.rfa.radio.document.model.EDocumentStatus;
import media.toloka.rfa.radio.document.repo.DocumentRepository;
import media.toloka.rfa.security.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ClientService clientService;

    public Documents GetDocument(Long id) {
        return documentRepository.findDocumentById(id);
    }

    public void saveDocument(Documents document) {
        documentRepository.save(document);
    }

    public List<Documents> listDocumentsByClientdetail(Clientdetail cl) {
        return documentRepository.findDocumentByClientdetail(cl) ;
    }

    public void saveDocumentInfo(Path destination) {
        Documents doc = new Documents();
        doc.setStatus(EDocumentStatus.STATUS_LOADED);
        doc.setPathToDocument(destination.getFileName().toString());
        doc.setClientdetail(clientService.getClientDetail(clientService.GetCurrentUser()));
        doc.setLoadDate(LocalDateTime.now());
        documentRepository.save(doc);
    }
}
