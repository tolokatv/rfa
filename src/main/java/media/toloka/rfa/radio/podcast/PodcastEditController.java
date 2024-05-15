package media.toloka.rfa.radio.podcast;


//import dev.stalla.builder.episode.EpisodeBuilder;
//import dev.stalla.model.Episode;
//import dev.stalla.model.MediaType;
//import dev.stalla.model.Podcast;
//import dev.stalla.model.rss.Enclosure;
//import dev.stalla.model.rss.RssCategory;
import com.rometools.rome.io.FeedException;
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

import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.SyndFeedOutput;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
/*  // todo тимчасово закоментував, щоб не входити кожного разу

        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }

        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        if (cd == null) { return "redirect:/"; }
*/
        logger.info("Зайшли на /podcast/pedit/{puuid}");
        PodcastChannel podcast;
        if (puuid.length() < 3) {
            // створюємо новий подкаст
            podcast = new PodcastChannel();
//            podcast.setClientdetail(cd); // todo повернути після отладки
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
        // http://localhost:3080/podcast/pedit/df61e94f-f74e-401b-9e81-c30a48982d21

        // Users user = clientService.GetCurrentUser();
/*
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }
        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        if (cd == null) { return "redirect:/"; }

        podcast.setClientdetail(cd);
*/

        PodcastChannel tpodcast = podcastService.GetChanelByUUID(podcast.getUuid());
//        if (tpodcast != null) {
        tpodcast.setTitle(podcast.getTitle());
        tpodcast.setDescription(podcast.getDescription());
        tpodcast.setLastbuilddate(new Date());
        podcastService.SavePodcast(tpodcast);
        // пробуємо побудувати RSS
        // -  https://github.com/mpgirro/stalla/tree/master?tab=readme-ov-file
        // https://rometools.github.io/rome/HowRomeWorks/RomeV0.4TutorialUsingRomeToCreateAndWriteASyndicationFeed.html
/*
пробуємо працювати з RSS feed для подкасту
 */
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");

        feed.setTitle("Sample Feed (created with Rome)");
        feed.setLink("http://rome.dev.java.net");
        feed.setDescription("This feed has been created using Rome (Java syndication utilities");

        List entries = new ArrayList();
        SyndEntry entry;
        SyndContent description;

        entry = new SyndEntryImpl();
        entry.setTitle("Rome v1.0");
        entry.setLink("http://wiki.java.net/bin/view/Javawsxml/Rome01");
        entry.setPublishedDate(new Date());
        description = new SyndContentImpl();
        description.setType("text/plain");
        description.setValue("Initial release of Rome");
        entry.setDescription(description);
        entries.add(entry);

        entry = new SyndEntryImpl();
        entry.setTitle("Rome v3.0");
        entry.setLink("http://wiki.java.net/bin/view/Javawsxml/Rome03");
        entry.setPublishedDate(new Date());
        description = new SyndContentImpl();
        description.setType("text/html");
        description.setValue("<p>More Bug fixes, mor API changes, some new features and some Unit testing</p>"+
                "<p>For details check the <a href=\"http://wiki.java.net/bin/view/Javawsxml/RomeChangesLog#RomeV03\">Changes Log</a></p>");
        entry.setDescription(description);
        entries.add(entry);

        feed.setEntries(entries);

        try {
            Writer writer = new FileWriter("/home/ysv/rss.xml");
            SyndFeedOutput output = new SyndFeedOutput();
            try {
                output.output(feed, writer);
                logger.info(output.outputString(feed));
            } catch (FeedException fe) {
                logger.info("Помилка 1 при запису RSS");
                logger.info(fe.getMessage());
            }
            writer.close();
        } catch (IOException e) {
            logger.info("Помилка 2 при запису RSS");
            logger.info(e.getMessage());
        }

        // TODO відправити повідомлення на сторінку
        model.addAttribute("success",  "Реакція на POST зі сторінки /podcast/proot");
        return "redirect:/podcast/home";
    }

}
