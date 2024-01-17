package media.toloka.rfa.radio.station.service;

import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.client.service.ClientService;

import media.toloka.rfa.radio.station.model.Station;
import media.toloka.rfa.radio.station.repo.StationRepo;
import media.toloka.rfa.security.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    public void saveStation(Station station) {
        stationRepo.save(station);
    }

    public Station GetStationById(long id) {
        return stationRepo.findById(id).get();
    }

    public void delete(long id) {
        stationRepo.deleteById(id);
    }

    public List<Station> GetListStationByUser(Users user) { // TODO Виправити.  На віддаленому сервері ми працюємо без користувача
        Clientdetail cl = clientService.getClientDetail(clientService.GetCurrentUser());
        return stationRepo.findStationByClientdetail(cl);
//        findStationByUser(user);
    }

    public Station CreateStation() {
        if(CheckPossibilityCreateStation(
                clientService.getClientDetail(
                        clientService.GetCurrentUser()
                )
            ) == true
        )
         {
            Station station = new Station();
            station.setName(null);
            station.setClientdetail(clientService.getClientDetail(clientService.GetCurrentUser()));
            station.setUuid(UUID.randomUUID().toString());
            station.setCreatedate(LocalDateTime.now());
            saveStation(station);
            return station;
        } else {
            return null;
        }
    }

    // Перевіряємо можливість створення станції
    private boolean CheckPossibilityCreateStation(Clientdetail clientDetail) {
        return true;
        // можна привʼязати станцію до контракту коли:
        // - не більше однієї станції на безкоштовному контракті
        // - Коли є платні контракти
    }

    private boolean CheckPossibilityLinkStation(Clientdetail clientDetail, Station station) {
        return true;
        // можна привʼязати станцію до контракту коли:
        // - не більше однієї станції на безкоштовному контракті
        // - Коли є платні контракти
    }

    private boolean CheckPossibilityStartStation(Clientdetail clientDetail) {
        return true;
        // можна стартувати станцію коли:
        // Це безкоштовна станція
        // Це оплачена станція на платному контракті
        // У станції проплачено нормальне імʼя
        // на рахунку достатньо коштів для роботи станції протягом 4-х тижнів
    }

//    public void saveStation(Station station) {
////        Users user = serviceUser.GetCurrentUser();
////        if (station.getUser() == null) {
////            station.setUser(user);
////        }
//        // TODO можливо потрібно перевірити на наявність станції
//        // TODO На віддаленому сервері ми працюємо без користувача. Інакше отримувати перелік станцій
//        // просто зберегли станцію
////        List<Station> listStations = GetListStationByUser(user);
////        if (station.getRadio_id() == null ) {
////            // Станція нова. Додаємо до переліку станцій
////            GetListRadioByUser(user).add(station);
////        }
//        stationRepo.save(station); // Зберігаємо станцію
//    }
}
