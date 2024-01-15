package media.toloka.rfa.radio.station.repo;


import media.toloka.rfa.radio.station.model.EServerPortType;
import media.toloka.rfa.radio.station.model.Poolport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoolPortRepo extends JpaRepository<Poolport, Long> {

    Poolport findFirstByPorttype(EServerPortType pt);

//    public void SavePort(Poolport port) {
//        save(port);
//    }

    Poolport findTopByOrderByPortDesc();
    default Integer findMaxPort() {
        Poolport top = findTopByOrderByPortDesc();
//        Poolport top = null;
        return top != null ? top.getPort()+1 : 8100;
    }

}
