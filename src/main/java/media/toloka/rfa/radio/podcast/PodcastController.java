package media.toloka.rfa.radio.podcast;


import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Station;
import media.toloka.rfa.radio.podcast.model.PodcastChannel;
import media.toloka.rfa.radio.podcast.model.PodcastItem;
import media.toloka.rfa.radio.podcast.service.PodcastService;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class PodcastController {
// стандарт RSS для подкаста
// https://podcast-standard.org/podcast_standard/

    @Autowired
    private PodcastService podcastService;
    @Autowired
    private ClientService clientService;

    final Logger logger = LoggerFactory.getLogger(PodcastController.class);


    @GetMapping(value = "/podcast/home")
    public String podcastroot(
            Model model ) {
        logger.info("Зайшли на /podcast/home");
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }
        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        if (cd == null) { return "redirect:/"; }

        List<PodcastChannel> podcastslist = podcastService.GetPodcastListByCd(cd);

        model.addAttribute("podcastslist",  podcastslist);
        if (podcastslist.isEmpty()) {
            model.addAttribute("warning", "Ви ще не маєте подкастів. Створіть свій перший подкаст!");
        }

        return "/podcast/home";
    }

    @GetMapping(value = "/podcast/view/{puuid}")
    public String podcastview(
            @PathVariable String puuid,
            Model model ) {
        logger.info("Зайшли на /podcast/view/{puuid}");
//        Users user = clientService.GetCurrentUser();
//        if (user == null) {
//            return "redirect:/";
//        }
//        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
//        if (cd == null) { return "redirect:/"; }



        PodcastChannel podcastChannel = podcastService.GetChanelByUUID(puuid);
        List<PodcastItem> podcastItems = podcastChannel.getItem();

        model.addAttribute("podcastChannel",  podcastChannel);
        model.addAttribute("podcastItems",  podcastItems);

        return "/podcast/view";
    }

    @PostMapping(value = "/podcast/home")
    public String podcastroot(
            @ModelAttribute Station station,
            @ModelAttribute Users formUserPSW,
            Model model ) {
            // Users user = clientService.GetCurrentUser();

            // TODO відправити повідомлення на сторінку
            model.addAttribute("success",  "Реакція на POST зі сторінки /podcast/proot");
        return "redirect:/podcast/home";
    }
}
