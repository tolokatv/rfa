package media.toloka.rfa.radio.podcast.service;

import media.toloka.rfa.radio.dropfile.service.FilesService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.podcast.model.PodcastChannel;
import media.toloka.rfa.radio.podcast.model.PodcastImage;
import media.toloka.rfa.radio.podcast.model.PodcastItem;
import media.toloka.rfa.radio.podcast.repositore.ChanelRepository;
import media.toloka.rfa.radio.podcast.repositore.EpisodeRepository;
import media.toloka.rfa.radio.podcast.repositore.PodcastCoverRepository;
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
    private PodcastCoverRepository coverPodcastRepository;

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

    public void SaveEpisodeUploadfile(String storeUUID, PodcastChannel podcast, Clientdetail cd) {
        // зберігаємо інформацію про завантажений епізод
        PodcastItem episode = new PodcastItem();
        episode.setChanel(podcast);
        episode.setStoreuuid(storeUUID);
        episode.setStoreitem(storeService.GetStoreByUUID(storeUUID));
        episode.setClientdetail(cd);
        podcast.getItem().add(episode);

        SavePodcast(podcast);

//        episodeRepository.save(episode);
    }


    public void SaveEpisode(PodcastItem episode) {
        episodeRepository.save(episode);
    }

    public void SaveCoverPodcastUploadfile(String storeUUID, PodcastChannel podcast, Clientdetail cd) {
        PodcastImage podcastImage = new PodcastImage();
        podcastImage.setStoreidimage(storeService.GetStoreByUUID(storeUUID));
        podcastImage.setClientdetail(cd);
        podcast.setImage(podcastImage);
        SavePodcast(podcast);
    }
    public void SaveCoverEpisodeUploadfile(String storeUUID, PodcastItem episode, Clientdetail cd) {
        PodcastImage itemImage = new PodcastImage();
        itemImage.setStoreidimage(storeService.GetStoreByUUID(storeUUID));
        itemImage.setClientdetail(cd);
        episode.setImage(itemImage);
        for (PodcastItem item : episode.getChanel().getItem()) {
            if (item.getId() == episode.getId() ) {
                item.setImage(itemImage);
                break;
            }
        }
        SavePodcast(episode.getChanel());
    }

    public List<PodcastItem> GetAllEpisodePaging(Clientdetail cd) {
        // findByClientdetailAndStorefiletypeOrderByIdDesc(cd,STORE_EPISODETRACK)
        return episodeRepository.findByClientdetailOrderByIdDesc(cd);
    }

    public List<PodcastImage> GetPodcastCoverListByCd(Clientdetail cd) {
        return coverPodcastRepository.findByClientdetailOrderByIdDesc(cd);
    }

    public PodcastImage GetImageByUUID(String iuuid) {
        return coverPodcastRepository.getByUuid(iuuid);
    }

    public List<PodcastChannel> GetPodcastListForRootCarusel() {
        return chanelRepository.findByApruve(true);
    }
}
