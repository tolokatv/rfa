package media.toloka.rfa.radio.document.service;

import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.model.enumerate.EDocumentStatus;
import media.toloka.rfa.radio.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import media.toloka.rfa.radio.model.Documents;

import java.nio.file.Path;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ClientService clientService;

    public Documents GetDocument(Long id) {
        return documentRepository.getById(id);
    }

    public void saveDocument(Documents document) {
        documentRepository.save(document);
    }

//    public List<Documents> listDocumentsFromClientdetail(Clientdetail cl) {
//        return documentRepository.findDocumentByClientdetail(cl) ;
//        return cl.getDocumentslist();
//    }
    public List<Documents> listDocumentsByClientdetail(Clientdetail cl) {
        List<Documents> ld = documentRepository.findByClientdetail(cl) ;
        return ld;
    }

    public void saveDocumentUploadInfo(Path destination) {
        Documents doc = new Documents();
        doc.setStatus(EDocumentStatus.STATUS_LOADED);
        doc.setPathToDocument(destination.getFileName().toString());
        doc.setClientdetail(clientService.GetClientDetailByUser(clientService.GetCurrentUser()));
        documentRepository.save(doc);
    }

    public List<Documents> GetUnApruvedDocumentsOrderLoaddate() {
//        return documentRepository.findByStatusLessThan(EDocumentStatus.STATUS_APPROVED);
//        findByStatusNot
        return documentRepository.findByStatusNot(EDocumentStatus.STATUS_APPROVED);
    }
}
