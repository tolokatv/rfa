package media.toloka.rfa.radio.messanger;

import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.message.service.MessageService;
import media.toloka.rfa.radio.messanger.model.ChatMessage;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Iterator;
import java.util.List;


@Controller
public class ChatController {
// https://kiberstender.github.io/miscelaneous-spring-websocket-stomp-specific-user/
// https://medium.com/@liakh-aliaksandr/websockets-with-spring-part-3-stomp-over-websocket-3dab4a21f397
// https://stomp-js.github.io/guide/stompjs/using-stompjs-v5.html

    @Autowired
    private ClientService clientService;

    private SimpMessagingTemplate template;

    @Autowired
    private MessageService messageService;

    @Autowired
    public ChatController(SimpMessagingTemplate template) {
        this.template = template;
    }

    final Logger logger = LoggerFactory.getLogger(ChatController.class);

        @MessageMapping("/hello")
        public void GetChatmessage( ChatMessage inmsg) {
//        public ChatMessage GetChatmessage( ChatMessage inmsg) {

            logger.info("Чат. Новий користувач онлайн: {} {}",inmsg.getUuid(),inmsg.getSend());
            try {
                PutChatPrivateMessage(inmsg);
//            } catch (Exception e) {
//                logger.info("PutChatPrivateMessage Exception");
//            }
            Clientdetail cd = clientService.GetClientDetailByUUID(inmsg.getUuid());
            List<Messages> publicMessageList = messageService.GetChatPublicRoomList(inmsg.getRoomuuid());
            ChatMessage cmsg = new ChatMessage();
            Iterator<Messages> iterator = publicMessageList.iterator();
            while (iterator.hasNext()) {
                Messages imsg = iterator.next();
                cmsg.setBody(imsg.getBody());
                cmsg.setFromuuid(imsg.getFrom().getUuid());
                cmsg.setFromname(imsg.getFrom().getCustname()+" "+imsg.getFrom().getCustsurname());
                cmsg.setToname(imsg.getTo().getCustname()+" "+imsg.getTo().getCustname());
                cmsg.setTouuid(imsg.getTo().getUuid());
                cmsg.setSend(imsg.getSend());
                cmsg.setRoomuuid(imsg.getRoomuuid());
                PutChatPublicMessage(cmsg);
            }
            List<Messages> privateMessageList = messageService.GetMessagesDesc(cd);

            Iterator<Messages> iteratorp = privateMessageList.iterator();
            while (iteratorp.hasNext()) {
                Messages imsg = iteratorp.next();
                cmsg.setBody(imsg.getBody());

                cmsg.setFromuuid(imsg.getFrom().getUuid());
                cmsg.setFromname(imsg.getFrom().getCustname()+" "+imsg.getFrom().getCustsurname());
                cmsg.setToname(imsg.getTo().getCustname()+" "+imsg.getTo().getCustname());
                cmsg.setTouuid(imsg.getTo().getUuid());
                cmsg.setSend(imsg.getSend());
                cmsg.setRoomuuid(null);
                PutChatPrivateMessage(cmsg);
            }
        } catch (Exception e) {
        logger.info("PutChatPullInitMessage Exception");
                e.printStackTrace();
    }
        }

    @MessageMapping("/topic")
    public void GetChatPublicmessage( ChatMessage inmsg) {
//        public ChatMessage GetChatmessage( ChatMessage inmsg) {


        try {
            if (inmsg.getRoomuuid() == null) {
                logger.info("Чат. Private message to:{} from:{}",inmsg.getToname(),inmsg.getFromname());
                PutChatPrivateMessage(inmsg);
            } else {
                logger.info("Чат. To Public : from:{} to room:{}",inmsg.getFromname(),inmsg.getRoomuuid());
                PutChatPublicMessage(inmsg);
            }

        } catch (Exception e) {
            logger.info("PutChatPrivateMessage Exception");
        }
    }

    @SendTo("/topic")
    public void PutChatPublicMessage(ChatMessage message) throws Exception {
            this.template.convertAndSend("/topic/"+message.getRoomuuid(), message);
            logger.info("Public={}",message.getRoomuuid());
            messageService.SaveMessageFromChat(message);
    }

        public void PutChatPrivateMessage(ChatMessage message) throws Exception {
            Clientdetail cd = clientService.GetClientDetailByUuid(message.getTouuid());
            message.setSend(new Date());
            if (cd != null) {
                this.template.convertAndSend("/topic/"+cd.getUuid(), message);
                this.template.convertAndSend("/topic/"+message.getUuid(), message);
                messageService.SaveMessageFromChat(message);
//                Messages msg = new Messages();
//                msg.setRead(null);
//                msg.setSend(new Date());
//                msg.setFrom(clientService.GetClientDetailByUUID(message.getUuid()));
//                msg.setTo(cd);
//                msg.setBody(message.getBody());
//                msg.setRoomuuid(message.getRoomuuid());
//                messageService.SaveMessage();

            }
        }

    @SendTo("/topic/public")
    public ChatMessage PutChatPrivMessage(ChatMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        ChatMessage cm = new ChatMessage();
        cm.setSend(new Date());

        return cm;
    }


}
