package media.toloka.rfa.radio.admin;


import media.toloka.rfa.radio.admin.service.AdminService;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.document.service.DocumentService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Documents;
import media.toloka.rfa.security.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class AdminDocuments {

    @Autowired
    private AdminService adminService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private DocumentService documentService;

    @GetMapping(value = "/admin/documents")
    public String getUserHome(
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }

        List<Documents> documentsList = adminService.GetNotApruvedDocuments();
//        List<Users> usersList = adminService.GetAllUsers();
        HashMap<Long, Integer> clientdetailLongHashMap = new HashMap<>();
//        Documents mdoc;
        List<Clientdetail> clientdetailList =  new ArrayList<>();
        Integer quantity;
        for (Documents doc : documentsList) {
            if (!clientdetailLongHashMap.containsKey(doc.getClientdetail().getId())) {
                Clientdetail cd = doc.getClientdetail();
                Integer sz = cd.getDocumentslist().size();
                clientdetailList.add(cd);
                clientdetailLongHashMap.put(cd.getId(), sz);
            }
        }

        model.addAttribute("usersList", adminService.GetAllUsers() );
        model.addAttribute("clientdetailList", clientdetailList );
        return "/admin/documents";
    }
}
