package media.toloka.rfa.radio.message.service;

import media.toloka.rfa.radio.history.repository.RepoHistory;
import media.toloka.rfa.radio.message.model.Messages;
import media.toloka.rfa.radio.message.repo.RepoMessages;
import media.toloka.rfa.radio.message.repo.RepoRooms;
import media.toloka.rfa.security.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MessageService {
    @Autowired
    private RepoMessages repoMessages;
    @Autowired
    private RepoRooms repoRooms;
    @Autowired
    private RepoHistory repoHistory;

    public List<Messages> GetMessages(Users user) {
        return repoMessages.findMessagesByUserfOrUsert(user, user);
    }
    public List<Messages> GetMessagesDesc(Users user) {
        return repoMessages.findMessagesByUserfOrUsertOrderBySendDesc(user, user);
    }

    public List<Messages> GetNewMessages(Users user) {
            return repoMessages.findMessagesByReadingAndUsert(true,user);
    }

    public void SendMessageToUser(Users userf, Users usert, String message) //from to message
    {
        // формуємо повідомлення
        Messages um = new Messages();
//        um.setUserf(userf);
//        um.setUsert(usert);
        um.setBody(message);
        um.setSend(LocalDateTime.now());
        um.setReading(true);
        // Надсилаємо повідомлення
        SaveMessage(um);
    }

    public void SaveMessage(Messages message) {
        repoMessages.save(message);
    }
}
