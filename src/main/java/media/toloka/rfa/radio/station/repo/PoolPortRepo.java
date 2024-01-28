package media.toloka.rfa.radio.station.repo;


import media.toloka.rfa.model.enumerate.EServerPortType;
import media.toloka.rfa.model.Poolport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoolPortRepo extends JpaRepository<Poolport, Long> {

    Poolport findFirstByPorttype(EServerPortType pt);

    Poolport findTopByOrderByPortDesc();
}
