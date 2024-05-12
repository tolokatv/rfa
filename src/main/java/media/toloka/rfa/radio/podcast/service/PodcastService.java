package media.toloka.rfa.radio.podcast.service;

import media.toloka.rfa.radio.podcast.model.PodcastChannel;
import media.toloka.rfa.radio.podcast.model.PodcastItem;
import media.toloka.rfa.radio.podcast.repositore.ChanelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PodcastService {

    @Autowired
    private ChanelRepository chanelRepository;

    public PodcastChannel GetChanelByUUID(String puuid) {
        return chanelRepository.getByUuid(puuid);
    }

    public void SavePodcast(PodcastChannel podcast) {
        chanelRepository.save(podcast);
    }
}
