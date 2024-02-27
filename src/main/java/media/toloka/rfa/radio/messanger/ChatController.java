package media.toloka.rfa.radio.messanger;

import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.messanger.model.ChatMessage;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.store.StoreSiteController;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;


@Controller
public class ChatController {
// https://kiberstender.github.io/miscelaneous-spring-websocket-stomp-specific-user/
// https://medium.com/@liakh-aliaksandr/websockets-with-spring-part-3-stomp-over-websocket-3dab4a21f397
// https://stomp-js.github.io/guide/stompjs/using-stompjs-v5.html

    @Autowired
    private ClientService clientService;

    private SimpMessagingTemplate template;

    @Autowired
    public ChatController(SimpMessagingTemplate template) {
        this.template = template;
    }

    final Logger logger = LoggerFactory.getLogger(ChatController.class);

        @MessageMapping("/hello")
//        @SendTo("/topic/public")
        public ChatMessage GetChatmessage( ChatMessage inmsg) {

            logger.info("Чат. Вхідне повідомлення: {}",inmsg);
            try {
                PutChatPrivateMessage(inmsg);
//                PutChatPrivMessage(inmsg);
            } catch (Exception e) {
                logger.info("PutChatPrivateMessage Exception");
            }
//            Users user = clientService.GetCurrentUser();
//            Clientdetail cd = clientService.GetClientDetailByUser(user);
            inmsg.setSend(new Date());
//            inmsg.setFromuuid(cd.getUuid());
//            inmsg.setFromname(cd.getCustname()+" "+cd.getCustsurname());
            return inmsg;
        }

        @SendTo("/topic/public")
        public void PutChatPrivateMessage(ChatMessage message) throws Exception {
//            Thread.sleep(1000); // simulated delay

//            ChatMessage cm = new ChatMessage();
//            cm.setBody("=== body ===");
//            cm.setTo(cd);
            Clientdetail cd = clientService.GetClientDetailByUuid(message.getTouuid());
            message.setSend(new Date());
//            this.template.convertAndSend("/topic/public", message);
            this.template.convertAndSend("/topic/"+cd.getUuid(), message);
//            return message;
        }

    @SendTo("/topic/public")
    public ChatMessage PutChatPrivMessage(ChatMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        ChatMessage cm = new ChatMessage();
        cm.setSend(new Date());

        return cm;
    }


}
