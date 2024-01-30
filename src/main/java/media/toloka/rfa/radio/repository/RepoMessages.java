package media.toloka.rfa.radio.repository;

import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RepoMessages extends JpaRepository<Messages, Long> {


    List<Messages> findMessagesByFromOrTo(Clientdetail userFrom, Clientdetail UserTo);

    List<Messages> findMessagesByReadingAndTo(boolean reading, Clientdetail cd );

    List<Messages> findMessagesByFromOrToOrderBySendDesc(Clientdetail userFrom, Clientdetail UserTo);

    List<Messages> findMessagesByReadAndTo(LocalDateTime ldt, Clientdetail Clientdetailrfa);
}
