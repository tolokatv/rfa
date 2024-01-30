package media.toloka.rfa.radio.repository;

import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.History;
import media.toloka.rfa.security.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepoHistory extends JpaRepository<History, Long> {
    List<History> findByClientdetail(Clientdetail clientdetail);
}
