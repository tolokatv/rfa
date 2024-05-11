package media.toloka.rfa.radio.podcast;


import media.toloka.rfa.radio.station.ClientHomeStationController;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PodcastController {

    final Logger logger = LoggerFactory.getLogger(PodcastController.class);


    @GetMapping(value = "/podcast/proot")
    public String userHomeStation(
            Model model ) {
        return "/podcast/proot";
    }

    @PostMapping(value = "/podcast/proot")

}
