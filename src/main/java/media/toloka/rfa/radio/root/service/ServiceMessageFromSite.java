package media.toloka.rfa.radio.root.service;

//import media.toloka.rfa.radio.model.MessageFromSite;
//import media.toloka.rfa.radio.repo.user.RepoMessageFromSite;
import media.toloka.rfa.radio.model.MessageFromSite;
import media.toloka.rfa.radio.repository.RepoMessageFromSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServiceMessageFromSite {

    @Autowired
    private RepoMessageFromSite repoMessageFromSite;

    public ServiceMessageFromSite(RepoMessageFromSite repo) {
        this.repoMessageFromSite = repo;
    }

    public List<MessageFromSite> listAll() {
        return repoMessageFromSite.findAll();
    }

    public void save(MessageFromSite message) {
        repoMessageFromSite.save(message);
    }

    public MessageFromSite get(long id) {
        return repoMessageFromSite.findById(id).get();
    }

    public void delete(long id) {
        repoMessageFromSite.deleteById(id);
    }
}
