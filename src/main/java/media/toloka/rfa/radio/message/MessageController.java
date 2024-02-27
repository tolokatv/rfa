package media.toloka.rfa.radio.message;


import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.model.Messages;
import media.toloka.rfa.radio.message.service.MessageService;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MessageController {

    @Value("${media.toloka.rfa.support}")
    private String supportaddress;
    @Autowired
    private MessageService messageService;

    @Autowired
    private ClientService clientService;


    private static Logger logger = LoggerFactory.getLogger(ErrorController.class);
    @GetMapping("/usermessage")
    public String GetMessageHome(
            @ModelAttribute Messages messages,
            Model model
    ) {

        logger.info("============== Message Controller ");
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            logger.warn("User not found. Redirect to main page");
            return "redirect:/";
        }
        // ====================== Готуємо інформацію для сторінки
        Clientdetail Clientdetailrfa = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        model.addAttribute("currentUserID", Clientdetailrfa.getId());

        List<Messages> tmplistAllMessage = messageService.GetMessagesDesc(Clientdetailrfa);

        List<Messages> listAllMessage = tmplistAllMessage.stream().filter(b -> b.getRoomuuid().isBlank())
                .collect(Collectors.toList());




        List<Messages> listNewMessages = messageService.GetNewMessages(Clientdetailrfa);
        model.addAttribute("listAllMessage",  listAllMessage);
        model.addAttribute("rd",  listAllMessage.size());
        model.addAttribute("nrd",  listNewMessages.size());
        messageService.SetReadingAllMessages(Clientdetailrfa);
        model.addAttribute("message", new Messages());
        return "/communication/usermessage";
    }

    @PostMapping(value = "/usermessage")
    public String PostMessageHome(
            @ModelAttribute Messages message,
            Model model
    ) {
        // TODO у майбутньому потрібно забезпечити надсилання повідомлень не тільки в підтримку.
        Users user = clientService.GetCurrentUser();
        Clientdetail cd = clientService.GetClientDetailByUser(user);
        Clientdetail cdto = clientService.GetClientDetailByUser(clientService.GetUserByEmail(supportaddress));
        messageService.SendMessageToUser(cd, cdto, message.getBody());
        model.addAttribute("rd",  messageService.GetMessages(cd).size());
        model.addAttribute("nrd",  messageService.GetNewMessages(cd).size());
        return "redirect:/usermessage";
    }

}
