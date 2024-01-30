package media.toloka.rfa.repository;

import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RepoMessages extends JpaRepository<Messages, Long> {


    List<Messages> findMessagesByFromOrTom(Clientdetail userFrom, Clientdetail UserTo);

    List<Messages> findMessagesByReadingAndTom(boolean reading, Clientdetail cd );

    List<Messages> findMessagesByFromOrTomOrderBySendDesc(Clientdetail userFrom, Clientdetail UserTo);

    List<Messages> findMessagesByReadAndTom(LocalDateTime ldt, Clientdetail Clientdetailrfa);
}
