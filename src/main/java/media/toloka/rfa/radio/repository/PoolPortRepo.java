package media.toloka.rfa.radio.repository;


import media.toloka.rfa.radio.model.enumerate.EServerPortType;
import media.toloka.rfa.radio.model.Poolport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoolPortRepo extends JpaRepository<Poolport, Long> {

    Poolport findFirstByPorttype(EServerPortType pt);

    Poolport findTopByOrderByPortDesc();
}
