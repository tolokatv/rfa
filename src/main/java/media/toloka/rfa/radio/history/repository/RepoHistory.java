package media.toloka.rfa.radio.history.repository;

import media.toloka.rfa.radio.history.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoHistory extends JpaRepository<History, Long> {
//    Optional<History> listByUser(User user);
}
