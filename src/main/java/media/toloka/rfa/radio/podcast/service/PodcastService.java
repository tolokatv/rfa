package media.toloka.rfa.radio.podcast.service;

import media.toloka.rfa.radio.dropfile.service.FilesService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.podcast.model.PodcastChannel;
import media.toloka.rfa.radio.podcast.model.PodcastItem;
import media.toloka.rfa.radio.podcast.repositore.ChanelRepository;
import media.toloka.rfa.radio.podcast.repositore.EpisodeRepository;
import media.toloka.rfa.radio.store.Service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PodcastService {

    @Autowired
    private ChanelRepository chanelRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private StoreService storeService;

    @Autowired
    private FilesService filesService;

    public PodcastChannel GetChanelByUUID(String puuid) {
        return chanelRepository.getByUuid(puuid);
    }

    public void SavePodcast(PodcastChannel podcast) {
        chanelRepository.save(podcast);
    }

    public List<PodcastChannel> GetPodcastListByCd(Clientdetail cd) {
        return chanelRepository.findByClientdetail(cd);
    }

    public PodcastItem GetEpisodeByUUID(String euuid) {
        return episodeRepository.getByUuid(euuid);
    }

    public void SaveEpisode(String storeUUID, PodcastChannel podcast, Clientdetail cd) {
        // зберігаємо інформацію про завантажений епізод
        PodcastItem episode = new PodcastItem();
        episode.setChanel(podcast);
        episode.setStoreuuid(storeUUID);
        episode.setStoreitem(storeService.GetStoreByUUID(storeUUID));
        podcast.getItem().add(episode);
        SavePodcast(podcast);

        episodeRepository.save(episode);
    }
}
