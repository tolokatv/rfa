package media.toloka.rfa.radio.messanger.service;

import media.toloka.rfa.radio.messanger.repository.MessangerRepository;
import media.toloka.rfa.radio.model.Clientaddress;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.MessageRoom;
import media.toloka.rfa.radio.model.Messages;
import media.toloka.rfa.radio.repository.MessageRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class MessangerService {

    @Autowired
    private MessangerRepository messangerRepository;

    @Autowired
    private MessageRoomRepository messageRoomRepository;

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
}
