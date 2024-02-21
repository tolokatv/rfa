package media.toloka.rfa.radio.admin;


import media.toloka.rfa.radio.admin.service.AdminService;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Post;
import media.toloka.rfa.rpc.service.RPCService;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class AdminUser {

    final Logger logger = LoggerFactory.getLogger(AdminUser.class);


    @Autowired
    private AdminService adminService;
    @Autowired
    private ClientService clientService;

    @GetMapping(value = "/admin/users")
    public String getAdmiUser(
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }

        List<Users> usersList = adminService.GetAllUsers();

        model.addAttribute("usersList", usersList );

        return "/admin/users";
    }

}
