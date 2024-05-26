package media.toloka.rfa.radio.admin;


import media.toloka.rfa.radio.admin.service.AdminService;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.history.service.HistoryService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Post;
import media.toloka.rfa.rpc.service.RPCService;
import media.toloka.rfa.security.model.ERole;
import media.toloka.rfa.security.model.Roles;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

import static media.toloka.rfa.radio.model.enumerate.EHistoryType.History_UserSendMailSetPassword;
import static media.toloka.rfa.radio.model.enumerate.EHistoryType.History_UserSuspend;
import static media.toloka.rfa.security.model.ERole.*;

@Controller
public class AdminUser {

    final Logger logger = LoggerFactory.getLogger(AdminUser.class);


    @Autowired
    private AdminService adminService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private HistoryService historyService;


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

    @GetMapping(value = "/admin/userdel/{iduser}")
    public String getAdminDelUser(
            @PathVariable Long iduser,
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }
        // todo перевірити на приналежність до групи адмінів.

        Users curuser = adminService.GetUsersById(iduser);
        List< Roles> rolesList = curuser.getRoles();
        rolesList.clear();
        // Обробка ClientDetail. Видаляємо всі списки

        //
//        clientService.SaveUser(curuser);
//        clientService.DeleteUser(curuser);



        List<Users> usersList = adminService.GetAllUsers();
        model.addAttribute("usersList", usersList );

        return "redirect:/admin/users";
    }

//    http://localhost:8080/admin/enableuser/2
    @GetMapping(value = "/admin/enableuser/{iduser}")
    public String getAdminEnableUser(
            @PathVariable Long iduser,
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }

        Users curuser = adminService.GetUsersById(iduser);
        List< Roles> rolesList = curuser.getRoles();
//        rolesList.clear();
        // Обробка ClientDetail. Видаляємо всі списки

        //
        clientService.SaveUser(curuser);
//        clientService.DeleteUser(curuser);



        List<Users> usersList = adminService.GetAllUsers();
        model.addAttribute("usersList", usersList );

        return "redirect:/admin/users";
    }

    @GetMapping(value = "/admin/useraddgroup/{iduser}/{idgroup}")
    public String getAdminEnableUser(
            @PathVariable Long iduser,
            @PathVariable Integer idgroup,
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }

        Users curuser = adminService.GetUsersById(iduser);
        if (clientService.checkRole (ROLE_ADMIN)) {
            List<Roles> rolesList = curuser.getRoles();
            Roles role = new Roles();
            switch (idgroup) {
                case 0:
                    role.setRole(ROLE_UNKNOWN);
                    curuser.getRoles().add(role);
                    break;
                case 1:
                    role.setRole(ROLE_USER);
                    curuser.getRoles().add(role);
                    break;
                case 2:
                    role.setRole(ROLE_CREATER);
                    curuser.getRoles().add(role);
                    break;
                case 3:
                    role.setRole(ROLE_MODERATOR);
                    curuser.getRoles().add(role);
                    break;
                case 4:
                    role.setRole(ROLE_EDITOR);
                    curuser.getRoles().add(role);
                    break;
                case 5:
                    role.setRole(ROLE_ADMIN);
                    curuser.getRoles().add(role);
                    break;
                default:
                    curuser.getRoles().clear();
            }
        } else {
            historyService.saveHistory(History_UserSuspend, "Спроба змінити групи користувача під лівим аккаунтом " , user);
            // todo заморозити користувача, який спробував змінити групу іншому користувачу
            //user.getClientdetail().
            return "redirect:/logout";
        }
        clientService.SaveUser(curuser);

        List<Users> usersList = adminService.GetAllUsers();
        model.addAttribute("usersList", usersList );

        return "redirect:/admin/users";
    }

}
