package media.toloka.rfa.radio.repository;


import media.toloka.rfa.radio.model.MessageRoom;
import media.toloka.rfa.radio.model.Poolport;
import media.toloka.rfa.radio.model.enumerate.EServerPortType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long> {

    MessageRoom getById(Long id);

}
