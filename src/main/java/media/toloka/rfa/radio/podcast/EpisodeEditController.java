package media.toloka.rfa.radio.podcast;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EpisodeEditController {

    final Logger logger = LoggerFactory.getLogger(PodcastController.class);

    @GetMapping(value = "/podcast/episodeedit/{puuid}")
    public String podcastroot(
            @PathVariable String puuid,
            Model model ) {
        logger.info("Зайшли на епізод: /podcast/episodedit/{}",puuid);


        return "/podcast/episodeedit";
    }


}
