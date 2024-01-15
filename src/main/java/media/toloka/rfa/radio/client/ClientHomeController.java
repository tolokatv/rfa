package media.toloka.rfa.radio.client;

import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.message.service.MessageService;
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
    private MessageService messageService;
    @GetMapping(value = "/user/user_page")
    public String userHome(
            Model model ) {

        // Додаемо сповіщення про не прочитані повідомлення
        Long numberNewMsg = Long.valueOf (messageService.GetNewMessages(clientService.GetCurrentUser()).size());
        if (numberNewMsg > 0) {
            model.addAttribute("danger", "У Вас є нові повідомлення: "
                    + numberNewMsg.toString()
            );
        }

        Authentication au;
        au = SecurityContextHolder.getContext().getAuthentication();
//        Optional<Users> opt = ClientService.getByEmail(au.getName());
        Users user = clientService.getByEmail(au.getName());
        // Якщо не залогінені, то переходимо на головну.
//        if (opt.isEmpty()) {
        if (user == null) {
            return "redirect:/";
        }
        // Витягуєм користувача
//        Users user = opt.get();
        // дивимося його групи
        // відповідним чином виводимо пункти меню
        // Заповнюємо поля для форми
        model.addAttribute("userID",    user.getId());
        model.addAttribute("userName",  au.getName());
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
            @ModelAttribute Users user,
            Model model
    ) {
        Long usri = user.getId();
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