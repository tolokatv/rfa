package media.toloka.rfa.radio.station.repo;


import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.station.model.Station;
import media.toloka.rfa.security.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepo extends JpaRepository<Station, Long> {
    List<Station> findStationByClientdetail(Clientdetail  clientdetail);

    Station save(Station station);

}
