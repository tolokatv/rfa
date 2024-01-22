package media.toloka.rfa.radio.client;

import lombok.extern.slf4j.Slf4j;

import media.toloka.rfa.radio.client.model.Clientaddress;
import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.history.service.HistoryService;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static media.toloka.rfa.radio.history.model.EHistoryType.History_UserChangeConfirmInfo;
import static media.toloka.rfa.radio.history.model.EHistoryType.History_UserInfoSave;

@Slf4j
@Controller
public class ClientHomeInfoController {

    final Logger logger = LoggerFactory.getLogger(ClientHomeInfoController.class);
//    @Autowired
//    private UserRepository userRepo;
//    @Autowired
//    private RepoRadio repoRadio;
//    @Autowired
//    private RepoAlbum repoAlbum;
//    @Autowired
//    private RepoTrack repoTrack;
    @Autowired
    private ClientService clientService;

    @Autowired
    private HistoryService historyService;


//    @Autowired
//    private RepoUserDetail repoUserDetail;

    @GetMapping(value = "/user/usereditinfo")
    public String getUserHomeInfo(
//            @ModelAttribute User user,
            Model model ) {
        Users frmuser = clientService.GetCurrentUser();
        if (frmuser == null) {
            return "redirect:/";
        }
        // Витягуєм користувача

        // дивимося його групи
        // відповідним чином виводимо пункти меню
        // Заповнюємо поля для форми
        Clientdetail userdetail = clientService.getClientDetail(frmuser);
        if ( userdetail == null) {
            logger.info("Додаємо UserDetail та UserAddress до структури користувача.");
            userdetail = new Clientdetail();
//            userdetail.setUserid(frmuser.getId());
//            userdetail.getAdresses()new List<UserAddress>);
            userdetail.setClientaddressList(new ArrayList<Clientaddress>());
            userdetail.getClientaddressList().add(new Clientaddress());
        }
//        else if (userdetail.getAdresses() == null) {
//            userdetail.setAdresses(new UserAddress());
//        }
        logger.info("Додали UserDetail та UserAddress до структури користувача. Заповнюємо атрибут форми");
        if (userdetail.getConfirminfo() == null) {
            userdetail.setConfirminfo(false);
        }
        List<Clientaddress> clientaddresses = userdetail.getClientaddressList();
//        model.addAttribute("custname",  frmuser.getCustname());
//        model.addAttribute("surname",   frmuser.getCustsurname());
//        model.addAttribute("name",      frmuser.getName());
        model.addAttribute("userdetail", userdetail );
        model.addAttribute("clientaddresses", clientaddresses );
//        model.addAttribute("address",   frmuser.getCustsurname());
//        model.addAttribute("formaddress",   frmuser.getUserDetail().getAdresses());
//        logger.info("Заповнили атрибути форми");
        return "/user/usereditinfo";
    }

    @PostMapping(value = "/user/infosave")
    public String postuserHomeInfo(
//            @ModelAttribute Users userform,
            @ModelAttribute Clientdetail fuserdetail,
//            @ModelAttribute Clientaddress faddress,
            Model model ) {
        // Витягуєм користувача
        Clientdetail curuserdetail = clientService.getClientDetailById(fuserdetail.getId());
        if (curuserdetail == null) {
//            logger.info("Додаємо UserDetail та UserAddress до структури користувача.");
            curuserdetail = new Clientdetail();
//            curuserdetail.setUserid(currentUser.getId());
        }
        curuserdetail.setFirmname( fuserdetail.getFirmname() );
        curuserdetail.setComments( // коментарій до інформації про користувача
                fuserdetail.getComments()
        );

        /*

        curuserdetail.getAdresses().setFirmname(
                fuserdetail .getAdresses().getFirmname()
        );
        curuserdetail.getAdresses().setStreet(
                fuserdetail.getAdresses().getStreet()
        );
        curuserdetail.getAdresses().setBuildnumber( // коментарій до інформації про адресу
                fuserdetail.getAdresses().getBuildnumber()
        );
        curuserdetail.getAdresses().setKorpus( // коментарій до інформації про адресу
               fuserdetail.getAdresses().getKorpus()
        );
        curuserdetail.getAdresses().setAppartment( // коментарій до інформації про адресу
                fuserdetail.getAdresses().getAppartment()
        );
        curuserdetail.getAdresses().setCityname( // коментарій до інформації про адресу
                fuserdetail.getAdresses().getCityname()
        );
        curuserdetail.getAdresses().setArea( // коментарій до інформації про адресу
                fuserdetail.getAdresses().getArea()
        );
        curuserdetail.getAdresses().setRegion( // коментарій до інформації про адресу
                fuserdetail.getAdresses().getRegion()
        );
        curuserdetail.getAdresses().setCountry( // коментарій до інформації про адресу
                fuserdetail.getAdresses().getCountry()
        );
        curuserdetail.getAdresses().setZip( // коментарій до інформації про адресу
                fuserdetail.getAdresses().getZip()
        );
        curuserdetail.getAdresses().setPhone( // коментарій до інформації про адресу
                fuserdetail.getAdresses().getPhone()
        );
        curuserdetail.getAdresses().setComment( // коментарій до інформації про адресу
                fuserdetail.getAdresses().getComment()
        );

        curuserdetail.setComments( // коментарій до інформації про користувача
                fuserdetail.getComments()
        );

        */

//        getUser.getUserDetail().getAdresses().setUser( // коментарій до інформації про адресу
//                userform.getUserDetail().getAdresses().getUser()
//        );
//        getUser.getUserDetail().setUser( // коментарій до інформації про адресу
//                userform.getUserDetail().іуеUser(user)
//        );
        // тестуємо
//        Boolean b1 = getUsers.getUserDetail().getAdresses().getConfirminfo();
//        Boolean b2 = userform.getUserDetail().getAdresses().getConfirminfo();
        if (curuserdetail.getConfirminfo() != fuserdetail.getConfirminfo()) {
            curuserdetail.setConfirminfo(
                    fuserdetail.getConfirminfo()
            ) ;
            // Зберігаємо історію
            historyService.saveHistory( History_UserChangeConfirmInfo, curuserdetail.getConfirminfo().toString(), curuserdetail.getUser() );
            curuserdetail.setConfirmDate(LocalDateTime.now());
        }
/*

        // Зберігаємо інформацію про користувача
*/
        clientService.SaveClientDetail(curuserdetail);
        historyService.saveHistory( History_UserInfoSave, "UserInfoSave", curuserdetail.getUser() );

        return "redirect:/user/user_page";
    }


}
