package media.toloka.rfa.radio.login;

// робота з поштою https://nuwanthafernando95.medium.com/spring-boot-email-template-with-thymeleaf-4f21ca437b52
// реєстрація користувача https://www.baeldung.com/registration-verify-user-by-email

//import jakarta.mail.MessagingException;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.email.model.Mail;
import media.toloka.rfa.radio.email.service.EmailSenderService;
import media.toloka.rfa.radio.history.service.ServiceHistory;
import media.toloka.rfa.radio.login.model.Token;
import media.toloka.rfa.radio.login.service.TokenService;
import media.toloka.rfa.radio.root.RootController;
import media.toloka.rfa.security.security.model.Roles;
import media.toloka.rfa.security.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.util.*;

import static media.toloka.rfa.radio.history.model.EHistoryType.*;
import static media.toloka.rfa.security.security.model.ERole.*;

//import static media.toloka.rfa.radio.model.EHistoryType.*;
//import static media.toloka.rfa.security.model.ERole.*;
//import static media.toloka.rfa.security.UserDetailsService.model.ERole.ROLE_USER;


@Controller
public class UserLoginController {

//    @Autowired
//    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private ClientService clientService;

    @Autowired
    private ServiceHistory serviceHistory;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

//    @Autowired
//    private EmailSenderService emailSenderService;

    @Autowired
    private TokenService serviceToken;

    Logger logger = LoggerFactory.getLogger(RootController.class);

    @GetMapping(value = "/login/route")
    public String userRouter (
            Model model
    ) {
        System.out.println("============ ROUTE to Group page ");
        if (clientService.checkRole (ROLE_USER)) {
            System.out.println("============ redirect to User page");
            return "redirect:/user/user_page";
        } else if (clientService.checkRole (ROLE_CREATER)) {
            System.out.println("============ redirect to Creator page");
            return "redirect:/creater/home";
        } else if (clientService.checkRole(ROLE_EDITOR)) {
            return "redirect:/editor/home";
        } else if (clientService.checkRole(ROLE_MODERATOR)) {
            return "redirect:/moderator/home";
        } else if (clientService.checkRole(ROLE_ADMIN)) {
            return "redirect:/admin/home";
        };
        // Йой! Щлсь пішло не так
        System.out.println("============ redirect to Logout page");
        return "redirect:/logout";
    }

    @GetMapping("/login/registerRadioUser")
    public String registerRadioUser(
            Model model
    ) {
        // створюємо List Для радіобутонів

        model.addAttribute("user", new Users());
        return "/login/registerRadioUser";
    }

    @PostMapping("/login/registerRadioUser")
    public String saveRegisterUser(
            @ModelAttribute Users userDTO,
            Model model
    ) {
        // Забираємо з форми email користувача
        String email = userDTO.getEmail();
        String frmgrp = userDTO.getEmail();
        // намагаємося знайти пошту в базі
        Optional<Users> opt = clientService.findUserByEmail(email);
        // перевіряємо, чи є цей емайл в базі
        if (opt.isEmpty()) {
            // додали групу для користувача
            // TODO Якщо у користувача декілька груп, то у формі передбачити вибір групи з якої працюємо.
            // Роль беремо зі форми. Визначається статусом радіобутона.
            Roles role = new Roles();
            String ttt = "User";
            switch (ttt) {
//            switch (userDTO.getName()) {
                case "User":
                    role.setRole(ROLE_USER);
                    userDTO.setRoles(new ArrayList<Roles>());
                    userDTO.getRoles().add(role);
                    break;
                case "Creater":
                    role.setRole(ROLE_CREATER);
                    userDTO.setRoles(new ArrayList<Roles>());
                    userDTO.getRoles().add(role);
                    break;
                default:
                    role.setRole(ROLE_UNKNOWN);
                    userDTO.setRoles(new ArrayList<Roles>());
                    userDTO.getRoles().add(role);
            }
            // зберігаємо користувача в базу
            userDTO.setPassword("*");
            clientService.saveUser(userDTO);
            Long idUser = clientService.findUserByEmail(userDTO.getEmail()).get().getId();

//            Long idUser = clientService.saveUser(userDTO);

            opt = clientService.findById(idUser);  // перевірити чи збережено користувача
            if (opt.isPresent()) {
                Users user = opt.get();
                // Записуємо подію в журнал історії

//                serviceUser.saveHistory(History_UserCreate,
//                        user.getName(),
//                        user);

                // формуємо токен та зберігаємо в базу
                String token = UUID.randomUUID().toString();
                serviceToken.createVerificationToken(user, token);

                // формуємо повідомлення для відправки поштою
                Mail mail;
                mail = new Mail();
//                mail = new Mail("","","",);
                mail.setTo(userDTO.getEmail());
                mail.setFrom("rfa@toloka.kiev.ua");
                mail.setSubject("Радіо для Всіх! Підтвердження реєстрації Вашої радіостанції на сервісі.");
                Map<String, Object> map1 = new HashMap<String, Object>();
//                map1.put("name",(Object) userDTO.getEmail());
//                map1.put("name", (Object) userDTO.getCustname() + " " + userDTO.getCustsurname()); // сформували імʼя та призвище для листа
                // TODO правильно сформувати імʼя для відсилання пошти.
                map1.put("name", (Object) "УВАГА!!! Штучно Сформоване імʼя"); // сформували імʼя та призвище для листа
                map1.put("confirmationUrl", (Object) "https://rfa.toloka.media/login/setUserPassword?token=" + token); // сформували для переходу адресу з токеном
                mail.setHtmlTemplate(new Mail.HtmlTemplate("/mail/registerSetPassword", map1)); // заповнили обʼєкт для відсилання пошти
                // пробуємо надіслати
                logger.info("ЗРОБИТИ ВІДПРАВКУ ПОШТИ!!!");

                // потрібно зробити нормальну обробку помилок пошти
//                try {
//                    emailSenderService.sendEmail(mail);
////                }
////                catch (MessagingException e) {
////                    System.out.println("========================== mail MessagingException");
//                } catch (IOException e) {
//                    System.out.println("========================== mail IOException");
//                }
//                catch (javax.mail.MessagingException e) {
//                    throw new RuntimeException(e);
//                }
//                Optional<Users> topt = clientService.findUserByEmail(email);
                /*
                 */

                // Готуємо інформацію для відображення для користувача у формі
//                String message = "Користувача '" + email + "' успішно збережено! Для продовження реєстрації перевірте свою пошту.";
//                model.addAttribute("msg", message);
                model.addAttribute("success", "Користувача '" + email + "' успішно збережено! Для продовження реєстрації перевірте свою пошту.");
            }
        } else { // знайшли таку пошту
            // формуємо повідомлення для форми
            // Готуємо інформацію для відображення для користувача у формі
//            String message = "Користувача '" + email + "' знайдено в базі.";
//            model.addAttribute("msg", message);
//            userDTO.setRoles(Arrays.asList(userDTO.getName()));
//            System.out.println("========================= Group " + userDTO.getName() + " ====================");
            model.addAttribute("danger", "Користувача '" + email + "' знайдено в базі.");
            String restorePSW = "Відновити пароль?";
            model.addAttribute("restorepsw", restorePSW);
        }

        return "/login/registerRadioUser";
    }

    @GetMapping("/login/restorePsw")
    public String restorePswGet(
            Model model
    ) {
        return "/login/restorepsw";
    }

    // Відновлення паролю користувача через електронну пошту
    @PostMapping("/login/restorePsw")
    public String restorePswPost(
            @ModelAttribute Users userDTO,
            Model model
    ) {
        // Забираємо з форми email користувача
        String email = userDTO.getEmail();
        // намагаємося знайти пошту в базі кристувачів
        Optional<Users> opt = clientService.findUserByEmail(email);
        // перевіряємо, чи є цей емайл в базі
        if (opt.isPresent()) {  // користувач є в базі
            // формуємо токен та зберігаємо в базу
            Users user = opt.get();
            // формуємо повідомлення для відправки поштою
            Mail mail;
            mail = new Mail();
            mail.setTo(userDTO.getEmail());
            mail.setFrom("rfa@toloka.kiev.ua");
            mail.setSubject("Радіо для Всіх! Відновлення паролю користувача.");
            Map<String, Object> map1 = new HashMap<String, Object>();
            Token token; // = tokenRepo.findByUser(user);
            if(serviceToken.findByUser(user) == null) {
                serviceToken.createVerificationToken(user, UUID.randomUUID().toString());
            }
            token = serviceToken.findByUser(user);
            String fname;
//            fname = user.getCustname() + " " + user.getCustsurname();
            fname = "УВАГА!!! Штучне сформоване Імʼя.";
            model.addAttribute("fname", fname);
            map1.put("fname", (Object) fname); // сформували імʼя та призвище для листа
            map1.put("confirmationUrl", (Object) "https://rfa.toloka.media/login/setUserPassword?token=" + token.getToken()); // сформували для переходу адресу з токеном
            mail.setHtmlTemplate(new Mail.HtmlTemplate("/mail/restorePsw", map1)); // заповнили обʼєкт для відсилання пошти
            // TODO потрібно зробити нормальну обробку помилок пошти
            logger.info("ЗРОБИТИ ВІДПРАВКУ ПОШТИ!!!");
//            try {
//                emailSenderService.sendEmail(mail);
//                serviceHistory.saveHistory(History_UserSendMailSetPassword, mail.getTo(), user);
//
//            } catch (IOException e) {
//                System.out.println("========================== mail IOException");
//                model.addAttribute("msg", "На пошту '" + email + "' не надіслано лист. ");
//                return "redirect:/error";
//            }
//            catch (javax.mail.MessagingException e) {
////                throw new RuntimeException(e);
//                System.out.println("========================== mail MessagingException");
//                model.addAttribute("msg", "На пошту '" + email + "' не надіслано лист. ");
//                return "redirect:/error";
//            }

//        } catch (MessagingException e) {
//                System.out.println("========================== mail MessagingException");
//                model.addAttribute("msg", "На пошту '" + email + "' не надіслано лист. ");
//                return "redirect:/error";
//            } catch (IOException e) {
//                System.out.println("========================== mail IOException");
//                model.addAttribute("msg", "На пошту '" + email + "' не надіслано лист. ");
//                return "redirect:/error";
//            } catch (MailSendException e) {
//                System.out.println("========================== mail MailSendException");
//                model.addAttribute("msg", "На пошту '" + email + "' не надіслано лист. ");
//                return "redirect:/error";
//            }
            // формуємо повідомлення для форми реєстрації
            model.addAttribute("success", "На пошту '" + email + "' надіслано лист. Для продовження відновлення паролю перевірте свою пошту.");
//            }
        } else { // не знайшли таку пошту
            // формуємо повідомлення для форми
//            String message = "Користувача '" + email + "' не знайдено в базі.";
//            model.addAttribute("danger", message);
            String restorePSW = "Зареєструвати нового користувача?";
            model.addAttribute("restorepsw", restorePSW);

            model.addAttribute("danger", "Користувача '" + email + "' не знайдено в базі.");
        }
        return "/login/restorepsw";
//        return "redirect:/";
    }

    @GetMapping("/login/setUserPassword")
    public String RegitrationConfirm(
            @RequestParam("token") String token,
            Model model
    ) {
        // Перевіряємо токен для і встановлюємо пароль для користувача

        model.addAttribute("htoken", token);
        return "/login/setUserPassword";
    }

    @PostMapping("/login/setUserPassword")
    public String RegitrationSavePassword(
            @RequestParam("psw") String psw,
            @RequestParam("htoken") String token,
            Model model
    ) {
        // Перевіряємо термін дії токена і встановлюємо пароль для користувача
        Token myToken = serviceToken.findByToken(token);
        if (myToken != null) {
            //TODO перевірити термін дії токена. якщо протерміновано, то вивести повідомлення і запропонувати повторити
            Users myuser = myToken.getUser();
            // перевірити чи не залочено користувача
//            if (myuser.isLock()) {
//                // формуємо повідомлення про те, що користувач залочен
//                model.addAttribute("msg", "Користувачу заборонені операції на цьому сервісі. Зверніться до адміністрації сервісу.");
//                return "redirect:/error";
//            }
            myuser.setPassword(passwordEncoder.encode(psw));
//            myuser.setLock(false);
//            myuser.setEnabled(true);

            clientService.saveUser(myuser);
            Long idt = clientService.findUserByEmail(myuser.getEmail()).get().getId();
//            Long idt = clientService.saveUser(myuser);
            serviceHistory.saveHistory(History_UserSetPassword, "Встановили новий пароль", myuser);

            serviceToken.delete(myToken);
        } else {
            // токен не найден
            // формуємо сторінку з рекомендаціями що робити.
            // 1. Ви помилилися при копіювання строки з адресою
            // 2. Посилання застаріле. Спробуйте відновити пароль
            System.out.println("========================== Set User Password: Token not found. ");
            model.addAttribute("msg", "Унікального посилання не знайдено. Спробуйте ще раз, або зверніться до адміністратора сервісу: rfa.toloka.kiev.ua");
            return "redirect:/error";
        }
        // світимо результат запису пароля і якщо все гаразд, то пропонуємо ввійти в систему
        return "redirect:/login";
//        return "/setUserPassword";
    }

    @PostMapping("/login/saveUser")
    public String saveUser(
            @ModelAttribute Users user,
            Model model
    ) { // тут напевно робота з поштою
        clientService.saveUser(user);
        Long id = clientService.findUserByEmail(user.getEmail()).get().getId();
//        Long id = clientService.saveUser(user);
        String message = "Користувача '" + id + "' збережено!";
        model.addAttribute("msg", message);
        return "/login/registerUser";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "/login/login";
    }

    @PostMapping("/login")
    public String loginUser(
            @ModelAttribute Users user,
            Model model
    ) {
//        System.out.println("========================== Login");
        return "redirect:/user/user";
    }
}