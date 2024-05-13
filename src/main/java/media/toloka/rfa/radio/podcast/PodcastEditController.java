package media.toloka.rfa.radio.podcast;


import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.model.Clientdetail;
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

import java.util.List;

@Controller
public class PodcastEditController {


    @Autowired
    private PodcastService podcastService;
    @Autowired
    private ClientService clientService;

    final Logger logger = LoggerFactory.getLogger(PodcastController.class);

    @GetMapping(value = "/podcast/pedit/{puuid}")
    public String PodcastChanel(
            @PathVariable String puuid,
            Model model ) {

        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }

        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        if (cd == null) { return "redirect:/"; }

        logger.info("Зайшли на /podcast/pedit/{puuid}");
        PodcastChannel podcast;
        if (puuid.length() < 3) {
            // створюємо новий подкаст
            podcast = new PodcastChannel();
            podcast.setClientdetail(cd);
//            podcastService.SavePodcast(podcast);
            model.addAttribute("success",  "Створили новий подкаст."
                    +" Збережіть його і після цього додайте епізоди до подкасту.");
        } else {
            // шукаємо за UUID подкасту
            podcast = podcastService.GetChanelByUUID(puuid);
            if (podcast == null ) {
                model.addAttribute("warning",  "Йой! Щось пішло не так - ми не знайшли Ваш Подкаст."
                        +" Зверніться будь ласка до служби підтримки");
            }
        }
        List<PodcastItem> itemList = podcast.getItem();
        if (itemList.size() == 0) {
            model.addAttribute("warning", "Ваш подкаст ще не має епізодів."
                    +" Завантажте будь ласка епізоди, заповніть в них необхідні поля та додаайте до них обкладинку.");
        }

        model.addAttribute("podcast",  podcast);
        model.addAttribute("itemslist",  itemList);
        return "/podcast/pedit";
    }

    @PostMapping(value = "/podcast/chanelsave")
    public String PodcastChanelSave (
            @ModelAttribute PodcastChannel podcast,
//            @ModelAttribute Users formUserPSW,
            Model model ) {
        // Users user = clientService.GetCurrentUser();
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }
        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        if (cd == null) { return "redirect:/"; }

        podcast.setClientdetail(cd);
        podcastService.SavePodcast(podcast);
        // TODO відправити повідомлення на сторінку
        model.addAttribute("success",  "Реакція на POST зі сторінки /podcast/proot");
        return "redirect:/podcast/home";
    }

}
