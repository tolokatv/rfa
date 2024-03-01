package media.toloka.rfa.radio.messanger;

import media.toloka.rfa.radio.admin.AdminPosts;
import media.toloka.rfa.radio.admin.service.AdminService;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.creater.service.CreaterService;
import media.toloka.rfa.radio.history.service.HistoryService;
import media.toloka.rfa.radio.messanger.service.MessangerService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.post.service.PostService;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MessangerController {

    @Autowired
    private MessangerService messangerService;
    @Autowired
    private AdminService adminService;

    @Autowired
    private CreaterService createrService;

    @Autowired
    private PostService postService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private HistoryService historyService;

    final Logger logger = LoggerFactory.getLogger(AdminPosts.class);

    @GetMapping(value = "/messanger")
    public String getUserHome(
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }

        Clientdetail cd = clientService.GetClientDetailByUser(user);

        List<Clientdetail> userlist = messangerService.FindAllCorrespondentsList(cd);

        model.addAttribute("userlist", userlist );
        model.addAttribute("curuuid", cd.getUuid() );
        model.addAttribute("curusername", cd.getCustname()+" "+cd.getCustsurname() );
        model.addAttribute("curroom", messangerService.GetChatRoomById(1L).getUuid() );
//        model.addAttribute("roomlist", messangerService.GetChatRoomList() );
//


        return "/messenger/messenger";
    }

    @GetMapping(value = "/messanger/info")
    public String getUserinfo(
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }

        Clientdetail cd = clientService.GetClientDetailByUser(user);

        List<Clientdetail> userlist = messangerService.FindAllCorrespondentsList(cd);

        model.addAttribute("userlist", userlist );
        model.addAttribute("curuuid", cd.getUuid() );

        return "/messenger/messenger";
    }
}
