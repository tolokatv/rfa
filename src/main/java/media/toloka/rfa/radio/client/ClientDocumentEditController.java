package media.toloka.rfa.radio.client;

import lombok.extern.slf4j.Slf4j;

import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.document.model.IDDocuments;
import media.toloka.rfa.radio.document.service.DocumentService;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Slf4j
@Controller
public class ClientDocumentEditController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private DocumentService documentService;

    final Logger logger = LoggerFactory.getLogger(ClientDocumentEditController.class);

    @GetMapping("/user/documentedit/{idDocument}")
    public String GetDocumentEdit(
            @PathVariable Long idDocument,
//            IDDocuments document,
            Model model
    ) {
        // Витягуєм користувача
        Users user = clientService.GetCurrentUser();
        if (user == null) { return "redirect:/"; }
        // витягуємо документ, який ми будемо редагувати і передаємо в форму
        IDDocuments doc = clientService.GetDocument(idDocument);
//        logger.info("Document: {}",doc);
//        doc.setDocumentType("Type document");
        model.addAttribute("document", doc);
        model.addAttribute("ide", idDocument);
        model.addAttribute("user", user);
        return ("/user/documentedit");
    }
    @PostMapping("/user/documentedit")
    public String PostDocumentEdit(
//            @ModelAttribute Long ide,
            @ModelAttribute IDDocuments document,
//            @ModelAttribute User user,
            Model model
    ) {
        Authentication au;
        au = SecurityContextHolder.getContext().getAuthentication();
        Users user = clientService.getByEmail(au.getName());
        // Якщо не залогінені, то переходимо на головну.
        if (user == null) {
            return "redirect:/";
        }
        IDDocuments curDocument = documentService.GetDocument(document.getId());
//        if (cur)
        curDocument.setDocumentType(document.getDocumentType());
        curDocument.setUserComment(document.getUserComment());
        documentService.saveDocument(curDocument);


        model.addAttribute("user", user);
        return ("redirect:/user/documents");
    }
}
