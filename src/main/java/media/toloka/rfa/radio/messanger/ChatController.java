package media.toloka.rfa.radio.messanger;

import media.toloka.rfa.radio.messanger.model.ChatMessage;
import media.toloka.rfa.radio.store.StoreSiteController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;


@Controller
public class ChatController {
// https://kiberstender.github.io/miscelaneous-spring-websocket-stomp-specific-user/
// https://medium.com/@liakh-aliaksandr/websockets-with-spring-part-3-stomp-over-websocket-3dab4a21f397
// https://stomp-js.github.io/guide/stompjs/using-stompjs-v5.html

    final Logger logger = LoggerFactory.getLogger(ChatController.class);

        @MessageMapping("/hello")
        public void GetChatmessage( ChatMessage inmsg) {

            logger.info("Чат. Вхідне повідомлення: {}",inmsg);
            try {
                PutChatPrivateMessage(inmsg);
            } catch (Exception e) {
                logger.info("PutChatPrivateMessage Exception");
            }

        }

        @SendTo("/topic/greetings")
        public ChatMessage PutChatPrivateMessage(ChatMessage message) throws Exception {
            Thread.sleep(1000); // simulated delay
            ChatMessage cm = new ChatMessage();

            return cm;
        }
//    @SendTo("/topic/greetings")
//    public ChatMessage PutChatPrivateMessage(ChatMessage message) throws Exception {
//        Thread.sleep(1000); // simulated delay
//        ChatMessage cm = new ChatMessage();
//
//        return cm;
//    }


}
