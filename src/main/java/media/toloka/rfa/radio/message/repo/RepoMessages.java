package media.toloka.rfa.radio.message.repo;

import media.toloka.rfa.radio.message.model.Messages;
import media.toloka.rfa.security.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepoMessages extends JpaRepository<Messages, Long> {


    List<Messages> findMessagesByUserfOrUsert(Users userFrom, Users UserTo);

    List<Messages> findMessagesByReadingAndUsert(boolean reading, Users UserTo );

    List<Messages> findMessagesByUserfOrUsertOrderBySendDesc(Users userFrom, Users UserTo);
}
