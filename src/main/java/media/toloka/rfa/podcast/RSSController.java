package media.toloka.rfa.podcast;

import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import media.toloka.rfa.podcast.model.PodcastChannel;
import media.toloka.rfa.podcast.model.PodcastItem;
import media.toloka.rfa.podcast.model.PodcastImage;
import media.toloka.rfa.podcast.service.PodcastService;
import media.toloka.rfa.radio.client.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
//@RequestMapping(path = "/rss", produces = "application/xml")
public class RSSController {

    @Autowired
    private PodcastService podcastService;
    @Autowired
    private ClientService clientService;

    final Logger logger = LoggerFactory.getLogger(PodcastController.class);

    /* Формуємо RSS для подкасту */
    @GetMapping(value = "/rss/{puuid}", produces = "application/xml")
    public String PodcastChanel(
            @PathVariable String puuid,
            Model model ) {
        PodcastChannel podcastChannel = podcastService.GetChanelByUUID(puuid);

        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType( "rss_2.0" );
        feed.setTitle(podcastChannel.getTitle() );
        feed.setLink( "https://rfa.toloka.media/podcast/view/"+puuid );
        feed.setDescription( podcastChannel.getDescription() );

        List<SyndEntry> entries = new ArrayList<SyndEntry>();
        SyndEntry entry = null;
        SyndContent description = null;

        /* цикл по епізодам */


        entry = new SyndEntryImpl();
        entry.setTitle( "Entry1" );
        entry.setLink( "http://example.com/entry1" );
        entry.setPublishedDate( new Date() );

        description = new SyndContentImpl();
        description.setType("text/html");
        description.setValue( "This is the content of entry 1." );
        entry.setDescription( description );

        entries.add( entry );
        feed.setEntries(entries);

        SyndFeedOutput syndFeedOutput = new SyndFeedOutput();

//        return feed;
//        String soutput;
        try {
//            String soutput = syndFeedOutput.outputString(feed);
            return syndFeedOutput.outputString(feed);
        } catch (FeedException e) {
            logger.warn("Щось пішло не так при формуванні XML :( ");
        }
//        model.addAttribute("podcast",  podcast);
//        model.addAttribute("itemslist",  itemList);
//        return "/podcast/pedit";
        return "";
    }

}
