package media.toloka.rfa.radio.messanger;

import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.message.service.MessageService;
import media.toloka.rfa.radio.messanger.model.ChatMessage;
import media.toloka.rfa.radio.messanger.service.MessangerService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.MessageRoom;
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
    private MessangerService messangerService;

    @Autowired
    public ChatController(SimpMessagingTemplate template) {
        this.template = template;
    }

    final Logger logger = LoggerFactory.getLogger(ChatController.class);

        @MessageMapping("/hello")
//        @SendTo("/hello")
        public void GetChatmessage( ChatMessage inmsg) {
//        public ChatMessage GetChatmessage( ChatMessage inmsg) {

            logger.info("Чат. Новий користувач онлайн: inmsg getUuid: {} getRoomuuid(): {}",inmsg.getUuid(),inmsg.getRoomuuid());
            try {
//                PutChatPrivateMessage(inmsg);
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
                this.template.convertAndSend("/hello/"+cd.getUuid(), cmsg);
//                logger.info("Public={} clientUUID=/hello/{}",cmsg.getRoomuuid(),cd.getUuid());
//                PutChatPublicMessage(cmsg);
            }
            List<Messages> privateMessageList = messageService.GetMessagesAsc(cd);
//            cd = clientService.GetClientDetailByUuid(inmsg.getTouuid());
            Iterator<Messages> iteratorp = privateMessageList.iterator();
            while (iteratorp.hasNext()) {
                Messages imsg = iteratorp.next();
                if (!imsg.isReading()) {
                    imsg.setReading(true);
                    imsg.setRead(new Date());
                    messageService.SaveMessage(imsg);
                }
                cmsg.setBody(imsg.getBody());
                cmsg.setFromuuid(imsg.getFrom().getUuid());
                cmsg.setFromname(imsg.getFrom().getCustname()+" "+imsg.getFrom().getCustsurname());
                cmsg.setToname(imsg.getTo().getCustname()+" "+imsg.getTo().getCustname());
                cmsg.setTouuid(imsg.getTo().getUuid());
                cmsg.setSend(imsg.getSend());
                cmsg.setRoomuuid(null);
                this.template.convertAndSend("/topic/"+cd.getUuid(), cmsg);
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
//            if (inmsg.getRoomuuid() == null) {
//                logger.info("Чат. Private message from:{}/{} to:{}/{}",inmsg.getFromname(),inmsg.getFromuuid(),inmsg.getToname(),inmsg.getTouuid());
//                PutChatPrivateMessage(inmsg);
//            } else {
            MessageRoom mr = messangerService.GetRoomNameByUuid(inmsg.getRoomuuid());
                logger.info("Чат. To Public : from:{}/{} to room:{}/{}",inmsg.getFromname(),inmsg.getFromuuid(),mr.getRoomname(),inmsg.getRoomuuid());
                PutChatPublicMessage(inmsg);
//            }

        } catch (Exception e) {
            logger.info("PutChatPrivateMessage Exception");
            e.printStackTrace();
        }
    }

    @MessageMapping("/private")
    public void GetChatPrivateMessage( ChatMessage inmsg) {
//        public ChatMessage GetChatmessage( ChatMessage inmsg) {

        try {
            logger.info("Чат. Private message from:{}/{} to:{}/{}",inmsg.getFromname(),inmsg.getFromuuid(),inmsg.getToname(),inmsg.getTouuid());
            PutChatPrivateMessage(inmsg);
        } catch (Exception e) {
            logger.info("PutChatPrivateMessage Exception");
            e.printStackTrace();
        }
    }

    @SendTo("/topic")
    public void PutChatPublicMessage(ChatMessage message) throws Exception {
            this.template.convertAndSend("/topic/"+message.getRoomuuid(), message);
            logger.info("Public={}",message.getRoomuuid());
            messangerService.SaveMessageFromChat(message);
    }

    public void PutChatPrivateMessage(ChatMessage message) throws Exception {
        logger.info("Private from={} to={}",message.getFromuuid(),message.getTouuid());
        Clientdetail cd = clientService.GetClientDetailByUuid(message.getTouuid());
        logger.info("Private topic cd.getUuid(): /topic/+{} from={}/{} to={}/{}",cd.getUuid(),message.getFromname(),message.getFromuuid(),message.getToname(),message.getTouuid());
        logger.info("Private topic message.getFromuuid(): /topic/{} from={}/{} to={}/{}",message.getTouuid(),message.getFromname(),message.getFromuuid(),message.getToname(),message.getTouuid());
        message.setSend(new Date());
        if (cd != null) {
            this.template.convertAndSend("/topic/"+cd.getUuid(), message);
            this.template.convertAndSend("/topic/"+message.getFromuuid(), message);
            messangerService.SaveMessageFromChat(message);
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
