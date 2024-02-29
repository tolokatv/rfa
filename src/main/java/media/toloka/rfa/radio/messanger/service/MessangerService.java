package media.toloka.rfa.radio.messanger.service;

import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.message.service.MessageService;
import media.toloka.rfa.radio.messanger.model.ChatMessage;
import media.toloka.rfa.radio.messanger.repository.MessangerRepository;
import media.toloka.rfa.radio.model.Clientaddress;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.MessageRoom;
import media.toloka.rfa.radio.model.Messages;
import media.toloka.rfa.radio.repository.MessageRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class MessangerService {

    @Autowired
    private MessangerRepository messangerRepository;

    @Autowired
    private MessageRoomRepository messageRoomRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ClientService clientService;

    public List<Clientdetail> FindAllCorrespondentsList(Clientdetail cd) {
        List<Messages> messagesList = messangerRepository.findMessagesByFromOrTo(cd, cd);

        List<Clientaddress> recipientList = new ArrayList<>();
        HashMap<Long, Integer> clientdetailLongHashMap = new HashMap<>();
        List<Clientdetail> clientdetailList =  new ArrayList<>();
        Clientdetail lcd;
        for (Messages msg : messagesList) {
            if (!clientdetailLongHashMap.containsKey(msg.getFrom().getId()) ) {
                lcd = msg.getFrom();
                clientdetailList.add(lcd);
                clientdetailLongHashMap.put(lcd.getId(), 1);
            }
            if (!clientdetailLongHashMap.containsKey(msg.getTo().getId()) ) {
                lcd = msg.getTo();
                clientdetailList.add(lcd);
                clientdetailLongHashMap.put(lcd.getId(), 1);
            }
        }
        return clientdetailList;

    }

    public MessageRoom GetChatRoomById(long l) {
        return messageRoomRepository.getById(l);
    }

    public List<MessageRoom> GetChatRoomList() {
        return messageRoomRepository.findAll();
    }

    public MessageRoom GetRoomNameByUuid(String roomuuid) {
        return messageRoomRepository.getByUuid(roomuuid);
    }


    public void SaveMessageFromChat(ChatMessage message) {
        Clientdetail cd = clientService.GetClientDetailByUuid(message.getFromuuid());
        Messages msg = new Messages();
        msg.setRead(null);
        msg.setSend(new Date());
        msg.setFrom(cd);
        if (msg.getRoomuuid() != null) {
            msg.setTo(clientService.GetClientDetailByUuid(message.getFromuuid()));
        } else {
            msg.setTo(clientService.GetClientDetailByUUID(message.getTouuid()));
        }
        cd = clientService.GetClientDetailByUUID(message.getTouuid());
//        msg.setTo(cd);
        msg.setBody(message.getBody());
        msg.setRoomuuid(message.getRoomuuid());
        messageService.SaveMessage(msg);
    }
}
