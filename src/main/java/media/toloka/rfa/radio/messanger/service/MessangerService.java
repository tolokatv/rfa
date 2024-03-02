package media.toloka.rfa.radio.messanger.service;

import media.toloka.rfa.radio.client.service.ClientService;
//import media.toloka.rfa.radio.message.service.MessageService;
import media.toloka.rfa.radio.messanger.model.ChatMessage;
import media.toloka.rfa.radio.messanger.repository.ChatRepository;
import media.toloka.rfa.radio.messanger.repository.MessangerRepository;
import media.toloka.rfa.radio.model.Clientaddress;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.messanger.model.MessageRoom;
import media.toloka.rfa.radio.model.Messages;
import media.toloka.rfa.radio.messanger.repository.MessageRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessangerService {

    @Autowired
    private MessangerRepository messangerRepository;

    @Autowired
    private MessageRoomRepository messageRoomRepository;

    @Autowired
    private ChatRepository chatRepository;

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
//        Clientdetail cd = clientService.GetClientDetailByUuid(message.getFromuuid());
        chatRepository.save(message);
    }

    public void AddChatUser(ChatMessage inmsg) {
        ChatReferenceSingleton chatReferenceSingleton = ChatReferenceSingleton.getInstance();
        Clientdetail cd = clientService.GetClientDetailByUUID(inmsg.getUuid());
        chatReferenceSingleton.GetUsersMap().put(inmsg.getUuid(),cd.getCustname()+' '+cd.getCustsurname());
    }

    public List<ChatMessage> GetChatPublicRoomList(String roomUUID) {
        return chatRepository.findByRoomuuidOrderBySendAsc(roomUUID);
    }

    public List<ChatMessage> GetMessagesAsc(String useruuid) {
        return chatRepository.findByPrivateMessageByUserUuid(useruuid);
    }

    public void SaveMessage(ChatMessage imsg) {
        chatRepository.save(imsg);
    }

    public void CheckUserLastLiveTime() {
        ChatReferenceSingleton chatReferenceSingleton = ChatReferenceSingleton.getInstance();
        Map<String, Date> lastLivetime = chatReferenceSingleton.GetUserLastLiveTime();
        Map<String, String> userlist = chatReferenceSingleton.GetUsersMap();
        Date curdate = new Date();
        for (Map.Entry<String, Date> entry : lastLivetime.entrySet()) {
            Long interval = curdate.getTime() - entry.getValue().getTime() ;
            if (interval > 12000L ) {
                userlist.remove(entry.getKey());
            }
        }
    }

    // todo сделать подсчет сообщений для показу в меню
    public int GetQuantityNewMessage(String uuid) {
        return 0;
    }

    public Object GetQuantityAllMessage(String uuid) {
        return 0;
    }
}
