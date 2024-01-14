package media.toloka.rfa.radio.history.service;

import media.toloka.rfa.radio.history.model.EHistoryType;
import media.toloka.rfa.radio.history.model.History;
import media.toloka.rfa.radio.history.repository.RepoHistory;
import media.toloka.rfa.security.security.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ServiceHistory {

    @Autowired
    private RepoHistory historyrepo;

    public void saveHistory(EHistoryType type,String message, Users user) {
        // Записуємо в журнал подію
        History history = new History();
        history.setDateAction(LocalDateTime.now());
        history.setHistoryType(type);
        history.setAction(type.label);
//        history.setUser(user);
        history.setComment( history.getDateAction().toString()
                + " - "
                + message);
        historyrepo.save(history);
    }

}
