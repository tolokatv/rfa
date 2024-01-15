package media.toloka.rfa.radio.station.service;

import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.client.service.ClientService;

import media.toloka.rfa.radio.station.model.Station;
import media.toloka.rfa.radio.station.repo.StationRepo;
import media.toloka.rfa.security.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StationService {

    @Autowired
    private StationRepo stationRepo;

    @Autowired
    private ClientService clientService;

    public List<Station> listAll() {
        return stationRepo.findAll();
    }

    public void saveRadio(Station station) {
        stationRepo.save(station);
    }

    public Station get(long id) {
        return stationRepo.findById(id).get();
    }

    public void delete(long id) {
        stationRepo.deleteById(id);
    }

    public List<Station> GetListStationByUser(Users user) {
        Clientdetail cl = clientService.getClientDetail(clientService.GetCurrentUser());
        return stationRepo.findStationByClientdetail(cl);
//        findStationByUser(user);
    }

    public void AddAndSaveRadio(Users user, Station station) {
//        Users user = serviceUser.GetCurrentUser();
//        if (station.getUser() == null) {
//            station.setUser(user);
//        }
        // TODO можливо потрібно перевірити на наявність станції
        List<Station> listStations = GetListStationByUser(user);
//        if (station.getRadio_id() == null ) {
//            // Станція нова. Додаємо до переліку станцій
//            GetListRadioByUser(user).add(station);
//        }
        stationRepo.save(station); // Зберігаємо станцію
    }
}
