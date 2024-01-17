package media.toloka.rfa.radio.history.service;

import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.history.model.EHistoryType;
import media.toloka.rfa.radio.history.model.History;
import media.toloka.rfa.radio.history.repository.RepoHistory;
import media.toloka.rfa.security.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Service
public class HistoryService {

    @Autowired
    private ClientService clientService;

    @Autowired
    private RepoHistory historyrepo;

    public void saveHistory(EHistoryType type, String message, Users user) {
        // Записуємо в журнал подію
        History history = new History();
        history.setDateAction(LocalDateTime.now());
        history.setHistoryType(type);
        history.setAction(type.label);
        if (user != null) { // TODO А що робити в мікросервісі, де брати поточного користувача?
            Clientdetail cd = clientService.getClientDetail(user);
            history.setClientdetail(cd);
        }

        history.setComment( history.getDateAction().toString()
                + " - "
                + message);
        historyrepo.save(history);
    }

}
