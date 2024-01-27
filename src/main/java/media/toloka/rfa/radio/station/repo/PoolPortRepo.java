package media.toloka.rfa.radio.station.repo;


import media.toloka.rfa.radio.station.model.EServerPortType;
import media.toloka.rfa.radio.station.model.Poolport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoolPortRepo extends JpaRepository<Poolport, Long> {

    Poolport findFirstByPorttype(EServerPortType pt);

    Poolport findTopByOrderByPortDesc();
}
