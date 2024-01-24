package media.toloka.rfa.radio.station.service;

import media.toloka.rfa.radio.contract.model.Contract;
import media.toloka.rfa.radio.contract.service.ContractService;
import media.toloka.rfa.radio.history.service.HistoryService;
import media.toloka.rfa.radio.station.ClientHomeStationController;
import media.toloka.rfa.service.RfaService;
import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.client.service.ClientService;

import media.toloka.rfa.radio.station.model.Station;
import media.toloka.rfa.radio.station.repo.StationRepo;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static media.toloka.rfa.radio.history.model.EHistoryType.History_StatiionCreate;
import static media.toloka.rfa.radio.history.model.EHistoryType.History_UserCreateContract;

@Service
@Transactional
public class StationService {

    @Autowired
    private StationRepo stationRepo;

    @Autowired
    private ClientService clientService;
    @Autowired
    private ContractService contractService;

    @Autowired
    private RfaService rfaService;

    @Autowired
    private HistoryService historyService;

    @Value("${media.toloka.rfa.server.libretime.guiserver}")
    private String guiserver;

    @Value("${media.toloka.rfa.station.basename}")
    private String basestationurl;

    final Logger logger = LoggerFactory.getLogger(ClientHomeStationController.class);


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

    public Station CreateStation(Model model) {
        if(CheckPossibilityCreateStation(clientService.getClientDetail(clientService.GetCurrentUser()),model ) == true ) {

            Station station = new Station();
            station.setName(null);
            station.setClientdetail(clientService.getClientDetail(clientService.GetCurrentUser()));
            SetStationDBName(station);
            station.setUuid(UUID.randomUUID().toString());
            station.setGuiserver(guiserver);
            station.setCreatedate(LocalDateTime.now());
            saveStation(station);
            Clientdetail cld = station.getClientdetail();
            cld.getStationList().add(station);
            clientService.SaveClientDetail(cld);
            // TODO запис в журнал
//            historyService.saveHistory(History_StatiionCreate, " Нова станція: "+station.getUuid(), clientService.getClientDetail().getUser());
            return station;
        } else {
            // не змогли створити станцію
            logger.info("З якоїсь причини не можемо створити станцію. Дивіться повідомлення. ");
            return null;
        }
    }

    public void SetStationDBName(Station st) {
        while (true) {
            String  rstring = rfaService.GetRandomString(16);
            if (getStationDBName(rstring) == null) {
                st.setDbname(rstring);
                return;
            }
        }
    }


    // Перевіряємо можливість створення станції
    private boolean CheckPossibilityCreateStation(Clientdetail clientDetail, Model model) {
        // model != null коли ми визиваємо з контролера, який повинен намалювати повідомлення

        // можна привʼязати станцію до контракту коли:
        // вже створена угода та заповнені персональні данні
        // - не більше однієї станції на безкоштовному контракті
        // - Коли є платні контракти

        //перевіряємо, чи створені угоди
        List<Contract> contractList = contractService.FindContractByClientDetail(clientDetail);
        if (contractList.isEmpty()) {
//        if (clientDetail.getContractList().isEmpty()) {
            logger.info("Відсутні контракти. Буде неможливо приєднати станцію. Створіть контракт.");
            if (model != null) {
                logger.info("Повідомлення для форми: Відсутні контракти. Буде неможливо приєднати станцію. Створіть контракт.");
            }
            // TODO запис в журнал
            // TODO формуємо повідомлення для форми.
            // TODO направляємо лист користувачу?
            return false;
        }
        if ( CheckFreeContract(clientDetail,model) ) {
//            clientDetail.getContractList().
            logger.info("Створюємо одну безкоштовну станцію для тестування сервісу та навчання.");
            return true;
        } else {
            logger.info("Відсутні контракти. Буде неможливо приєднати станцію. Створіть контракт.");
            if (model != null) {
                logger.info("Повідомлення для форми: Відсутні контракти. Буде неможливо приєднати станцію. Створіть контракт.");
            }
//            return false;
        }
        if ( CheckPayContract(clientDetail, model) ) {
            logger.info("Можемо створити комерційну станцію.");
            return true;
        } else {
            logger.info("Відсутні комерційні контракти. Буде неможливо приєднати станцію. Створіть комерційний контракт.");
            if (model != null) {
                logger.info("Повідомлення для форми: Відсутні комерційні контракти. Буде неможливо приєднати станцію. Створіть комерційний контракт.");
            }
//            return false;
        }
        return false;
        // - Коли є платні контракти
    }

//    private List<Station> FindStationByClientDetail(Clientdetail clientDetail) {
//        return stationRepo.findStationByClientdetail(clientDetail.getId());
//    }


    private boolean CheckPayContract(Clientdetail clientDetail,Model model) {
        // перевіряємо наявність платних контрактів і можливість приєднати до них станцію
        // У разі неможливості - формуємо відповідне повідомлення для форми

        // Дивимося, чи є комерційні контракти

//        logger.info("Відсутні платні контракти. Буде неможливо приєднати ще одну станцію.");
//        if (model != null) {
//            logger.info("Повідомлення для форми: Відсутні платні контракти. Буде неможливо приєднати ще одну станцію.");
//        }
        return true;
    }

    private boolean CheckFreeContract(Clientdetail clientDetail, Model model) {
        // model != null коли ми визиваємо з контролера, який повинен намалювати повідомлення
        // перевіряємо можливість приєднання станції до наявних контрактів
//        logger.info("Відсутні платні контракти. Буде неможливо приєднати ще одну станцію.");
//        if (model != null) {
//            logger.info("Повідомлення для форми: Відсутні платні контракти. Буде неможливо приєднати ще одну станцію.");
//        }
        return true;
    }

    private boolean CheckPossibilityLinkStation(Clientdetail clientDetail, Station station, Model model) {
        // model != null коли ми визиваємо з контролера, який повинен намалювати повідомлення
        return true;
        // можна привʼязати станцію до контракту коли:
        // - не більше однієї станції на безкоштовному контракті
        // - Коли є платні контракти
    }

    private boolean CheckPossibilityStartStation(Clientdetail clientDetail, Model model) {
        // model != null коли ми визиваємо з контролера, який повинен намалювати повідомлення

        return true;
        // можна стартувати станцію коли:
        // Це одна безкоштовна станція
        // Це оплачена станція на платному контракті
        // У станції проплачено нормальне імʼя
        // на рахунку достатньо коштів для роботи станції протягом 4-х тижнів
    }

    public Station getStationDBName(String rstring) {
        // перевіряємо, чи є станція з таким імʼям бази для Libretime
        return stationRepo.getStationByDbname(rstring);
    }

    public Object GetURLStation(Station station) {
        // формуємо актуальний лінк на станцію
        String stationLinkURL;
        // TODO передбачити нормальну назву станції
        stationLinkURL = "https://" + station.getDbname() + "." + basestationurl;
        return stationLinkURL;
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
