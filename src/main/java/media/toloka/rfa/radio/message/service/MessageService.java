package media.toloka.rfa.radio.message.service;

import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Messages;
import media.toloka.rfa.radio.repository.RepoHistory;
import media.toloka.rfa.radio.repository.RepoMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.*;

@Service
@Transactional
public class MessageService {
    @Autowired
    private RepoMessages repoMessages;
//    @Autowired
//    private MessangerService messangerService;
    @Autowired
    private RepoHistory repoHistory;

    @Autowired
    private ClientService clientService;


    public List<Messages> GetMessages(Clientdetail clientdetail) {
        return repoMessages.findMessagesByFromOrTo(clientdetail, clientdetail);
    }

    public List<Messages> GetMessagesAsc(Clientdetail clientdetail) {
        List<Messages> tmplist = repoMessages.findMessagesByFromOrToOrderBySendAsc(clientdetail, clientdetail);


        Iterator<Messages> iterator = tmplist.iterator();
        List<Messages> resultlist = new ArrayList<>();

        while (iterator.hasNext()) {
            Messages imsg = iterator.next();
            if (imsg.getRoomuuid() == null) {
                resultlist.add(imsg);
            }
        }

        return resultlist;
    }

    // отримали загальну кількість повідомлень для клієнта.
    public int GetQuantityAllMessage(Clientdetail clientdetail) {
        return GetMessages(clientdetail).size();
    }

    // отримали загальну кількість нових повідомлень для клієнта.
    public int GetQuantityNewMessage(Clientdetail clientdetail) {
        return GetNewMessages(clientdetail).size();
    }

    public List<Messages> GetNewMessages(Clientdetail clientdetail) {
//            return repoMessages.findMessagesByReadingAndTom(true,clientdetail);
        List<Messages> msg = repoMessages.findMessagesByReadAndTo(null, clientdetail);
        return msg;
    }

    public void SendMessageToUser(Clientdetail from, Clientdetail to, String message) //from to message
    {
        // формуємо повідомлення
        Messages um = new Messages();
        um.setBody(message);
        um.setSend(new Date());
        um.setReading(true);
        um.setFrom(from);
        um.setTo(to);
        um.setRoomuuid(null);
        // Надсилаємо повідомлення
        SaveMessage(um);
    }

    public void SaveMessage(Messages message) {

        repoMessages.save(message);
    }

    public void SetReadingAllMessages(Clientdetail cd) {

        List<Messages> listNewMessages = repoMessages.findMessagesByReadingAndTo(true, cd);
        ListIterator<Messages> listIterator = listNewMessages.listIterator();

        while (listIterator.hasNext()) {
            Messages msg = listIterator.next();
            if (msg.getRead() != null) {
                msg.setReading(false);
            }
            msg.setRead(new Date());
            SaveMessage(msg);
        }
    }

    // встановлюємо для меню повідомлення/нові
    public void setNavQuantityMessage(Model model, Clientdetail clientDetail) {
        model.addAttribute("quantityallmessage", GetQuantityAllMessage(clientDetail));
        model.addAttribute("quantitynewmessage", GetQuantityNewMessage(clientDetail));

    }


}


