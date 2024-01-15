package media.toloka.rfa.radio.client;

import lombok.extern.slf4j.Slf4j;

import media.toloka.rfa.radio.client.model.Clientaddress;
import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

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

//    @Autowired
//    private RepoUserDetail repoUserDetail;

    @GetMapping(value = "/user/usereditinfo")
    public String getUserHomeInfo(
//            @ModelAttribute User user,
            Model model ) {
        Users frmuser = clientService.GetCurrentUser();
        if (frmuser == null) { // TODO перевірити щодо отримання значення у разі незалогіненого користувача
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
//        model.addAttribute("custname",  frmuser.getCustname());
//        model.addAttribute("surname",   frmuser.getCustsurname());
//        model.addAttribute("name",      frmuser.getName());
//        model.addAttribute("fuserdetail", userdetail );
//        model.addAttribute("address",   frmuser.getCustsurname());
//        model.addAttribute("formaddress",   frmuser.getUserDetail().getAdresses());
//        logger.info("Заповнили атрибути форми");
        return "/user/usereditinfo";
    }

    @PostMapping(value = "/user/infosave")
    public String postuserHomeInfo(
            @ModelAttribute Users userform,
            @ModelAttribute Clientdetail fuserdetail,
            @ModelAttribute Clientaddress faddress,
            Model model ) {
        // Витягуєм користувача
        Users currentUser = clientService.GetCurrentUser();
        if (currentUser == null) {
            return "redirect:/"; //якийсь не правильний користувач. Відправляємо на головну сторінку.
        }
        Clientaddress ua;
        Clientdetail curuserdetail = clientService.getClientDetail(currentUser);
        // Заповнюємо поля для запису в базу
        if (curuserdetail == null) {
//            logger.info("Додаємо UserDetail та UserAddress до структури користувача.");
            curuserdetail = new Clientdetail();
            ua = new Clientaddress();
//            curuserdetail.setUserid(currentUser.getId());
        }
        if (curuserdetail.getClientaddressList().size() == 0) {
            ua = new Clientaddress();
        }
//        else {
//
//            // Знайти тип адреси. Якщо немає, то додамо нову адресу.
//
//        }
        /*
        curuserdetail.getAdresses().add(ua);

        curuserdetail.getAdresses().setFirmname(
                fuserdetail .getAdresses().getFirmname()
        );
        curuserdetail.getAdresses().setFirmname(
                fuserdetail.getAdresses().getFirmname()
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
/*
        if (curuserdetail.getAdresses().getConfirminfo() != fuserdetail.getAdresses().getConfirminfo()) {
            curuserdetail.getAdresses().setConfirminfo(
                    fuserdetail.getAdresses().getConfirminfo()
            ) ;
            // Зберігаємо історію
            serviceUser.saveHistory( // працює
                    History_UserChangeConfirmInfo,
                    curuserdetail.getAdresses().getConfirminfo().toString(),
                    getUsers
                    );
            curuserdetail.getAdresses().setConfirmDate(LocalDateTime.now());


        }

        // Зберігаємо інформацію про користувача

        repoUserDetail.save(curuserdetail);
        serviceUser.saveHistory( // працює
                History_UserInfoSave,
                "UserInfoSave",
                getUsers
        );
*/
        return "redirect:/user/user_page";
    }


}
