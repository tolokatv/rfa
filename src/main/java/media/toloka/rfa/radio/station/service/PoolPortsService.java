package media.toloka.rfa.radio.station.service;


import media.toloka.rfa.radio.model.enumerate.EServerPortType;
import media.toloka.rfa.radio.model.Poolport;
import media.toloka.rfa.radio.model.Station;
import media.toloka.rfa.radio.repository.PoolPortRepo;
import media.toloka.rfa.radio.repository.StationRepo;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static media.toloka.rfa.radio.model.enumerate.EServerPortType.PORT_FREE;


// сервіс, який обслуговує пулл портів для радіостанцій
@Service
public class PoolPortsService {

    @Value("${media.toloka.rfa.server.libretime.firstguiport}")
    private Integer firstguiport;

    @Value("${media.toloka.rfa.server.libretime.firstshowport}")
    private Integer firstshowport;
    // TODO Рознести порти для трансляцій та GUI

    final Logger logger = LoggerFactory.getLogger(PoolPortsService.class);

    @Autowired
    private PoolPortRepo poolPortRepo;

    @Autowired
    private StationRepo stationRepo;

    public Station AttachPort(Users user, Station station, EServerPortType portType) {
        //
        Poolport sp = GetFreePort();
        if (sp == null) {
            sp = GetNewFreePort();
        }
        sp.setPorttype(portType);
        switch  (portType) {
                case PORT_MAIN:
                    station.setMain(sp.getPort());
                    logger.info("MAIN призначення нового порту станції");
                    break;
                case PORT_WEB:
                    station.setGuiport(sp.getPort());
                    logger.info("WEB призначення нового порту станції");
                    break;
                case PORT_SHOW:
                    station.setShow(sp.getPort());
                    logger.info("SHOW призначення нового порту станції");
                    break;
                default:
                    logger.info("Помилка при призначенні нового порту станції");
                    return null;
            }
        sp.setStation(station);
        SavePort(sp);
        return  station;
    }

    private Poolport GetFreePort () {
        // дивимося, чи є вільні порти EServerPortType PORT_FREE
        Poolport firstFreePorts = poolPortRepo.findFirstByPorttype( PORT_FREE);
        if (firstFreePorts!= null ) {
            return firstFreePorts;
        } else {
            return GetNewFreePort(); // TODO Що робити коли вийшли за межі портів на сервері?
        }
    }

    private Poolport GetNewFreePort() {
        // вибираємо максимальний номер не зайнятого порту.
        Integer newPort = findMaxPort();
        Poolport sp = new Poolport();
        sp.setPort(newPort);
        return sp;
    }

    private Integer findMaxPort() {
        Poolport top = poolPortRepo.findTopByOrderByPortDesc();
        if (top != null) { return top.getPort()+1; } else { return firstguiport; }
    }

    public void SavePort(Poolport port) {
        poolPortRepo.save(port);
    }

}
