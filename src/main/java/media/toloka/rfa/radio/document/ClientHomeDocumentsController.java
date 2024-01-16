package media.toloka.rfa.radio.document;

import lombok.extern.slf4j.Slf4j;

import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.document.model.Documents;
import media.toloka.rfa.radio.document.service.DocumentService;
import media.toloka.rfa.security.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
public class ClientHomeDocumentsController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private DocumentService documentService;

    @GetMapping(value = "/user/documents")
    public String userHome(
            Model model) {
        Authentication au;
//        au = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = clientService.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        // Якщо не залогінені, то переходимо на головну.
        if (user == null) {
            return "redirect:/";
        }
        // дивимося його групи
        // відповідним чином виводимо пункти меню
        // Заповнюємо поля для форми
        List<Documents> documents = documentService.listDocumentsByClientdetail(
                clientService.getClientDetail(
                        clientService.GetCurrentUser()
                )
        );

        //==========================================================
        // Бавимося
        //==========================================================
        model.addAttribute("documents", documents);
        model.addAttribute("userID", user.getId());
        model.addAttribute("userName", user.getEmail());
        return "/user/documents";
    }

    @GetMapping(value = "/user/userloaddocument")
    public String userLoadDocument(
            Model model) {
//        return "redirect:/"
        return "redirect:/uploadfile";
    }
}