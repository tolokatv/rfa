package media.toloka.rfa.radio.client;

import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.message.service.MessageService;
import media.toloka.rfa.radio.station.service.StationService;
import media.toloka.rfa.security.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@Controller
public class ClientHomeController {

//    @Autowired
//    private UserRepository userRepo;

    @Autowired
    private ClientService clientService;

    @Autowired
    private StationService stationService;

    @Autowired
    private MessageService messageService;
    @GetMapping(value = "/user/user_page")
    public String userHome(
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("stations",  stationService.GetListStationByUser(user));
//        model.addAttribute("userID",    user.getId());
//        model.addAttribute("userName",  user.getName());
        return "/user/user_page";
    }

    @GetMapping(value = "/user/home/documents")
    public String UserManageDocuments(
            @ModelAttribute Users user,
            Model model
    ) {
        Long usri = user.getId();
        return "redirect:/user/documents";
    }

    @GetMapping(value = "/user/home/managestations")
    public String UserManageStation(
//            @ModelAttribute Users user,
            Model model
    ) {
//        Long usri = user.getId();
        return "redirect:/user/stations";
    }

    @GetMapping(value = "/user/home/managecontract")
    public String UserManageContract(
            @ModelAttribute Users user,
            Model model
    ) {
        Long usri = user.getId();
        return "redirect:/user/contract";
    }

    @GetMapping(value = "/user/home/usergetinfo")
    public String UserGetInfo(
            @ModelAttribute Users user,
            Model model
    ) {
        Long usri = user.getId();
        return "redirect:/user/usereditinfo";
    }

}