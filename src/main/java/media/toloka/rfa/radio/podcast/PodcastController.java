package media.toloka.rfa.radio.podcast;


import media.toloka.rfa.radio.model.Station;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PodcastController {

    final Logger logger = LoggerFactory.getLogger(PodcastController.class);


    @GetMapping(value = "/podcast/home")
    public String podcastroot(
            Model model ) {
        logger.info("Зайшли на /podcast/home");
        return "/podcast/home";
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
