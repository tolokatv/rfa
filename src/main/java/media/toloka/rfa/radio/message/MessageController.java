package media.toloka.rfa.radio.message;


import media.toloka.rfa.model.Clientdetail;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.model.Messages;
import media.toloka.rfa.radio.message.service.MessageService;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MessageController {
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
        Clientdetail clientdetail = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        model.addAttribute("currentUserID", clientdetail.getId());
        List<Messages> listAllMessage = messageService.GetMessagesDesc(clientdetail);
        List<Messages> listNewMessages = messageService.GetNewMessages(clientdetail);
        model.addAttribute("listAllMessage",  listAllMessage);
        model.addAttribute("rd",  listAllMessage.size());
        model.addAttribute("nrd",  listNewMessages.size());
        messageService.SetReadingAllMessages(clientdetail);
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
        messageService.SendMessageToUser(cd, null, message.getBody());
        model.addAttribute("rd",  messageService.GetMessages(cd).size());
        model.addAttribute("nrd",  messageService.GetNewMessages(cd).size());
        return "redirect:/usermessage";
    }

}
