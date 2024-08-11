package media.toloka.rfa.podcast.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import media.toloka.rfa.podcast.PodcastController;
import media.toloka.rfa.podcast.model.PodcastItunesCategory;
import media.toloka.rfa.podcast.repositore.ItunesCategoryRepository;
import media.toloka.rfa.radio.dropfile.service.FilesService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.podcast.model.PodcastChannel;
import media.toloka.rfa.podcast.model.PodcastImage;
import media.toloka.rfa.podcast.model.PodcastItem;
import media.toloka.rfa.podcast.repositore.ChanelRepository;
import media.toloka.rfa.podcast.repositore.EpisodeRepository;
import media.toloka.rfa.podcast.repositore.PodcastCoverRepository;
import media.toloka.rfa.radio.store.Service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PodcastService {

    final Logger logger = LoggerFactory.getLogger(PodcastService.class);


    @Autowired
    private ChanelRepository chanelRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private ItunesCategoryRepository itunesCategoryRepository;

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

    public List<PodcastChannel> GetAllChanel() {
        List<PodcastChannel> listCh = chanelRepository.findAll();
        return listCh;
    }

    public String GetEpisodeNumberComments(PodcastItem item) {
        // кількість коментарів для епізоду подкасту
        return "0";
    }

    // читаємо категорії itunes для подкасту з файлу, який розташовано в ресурсах
    public Map<String,List<String>> ItunesCatrgory() {

        String resource = "itunes.json";
        String jsonString;
        try {
            File file = ResourceUtils.getFile("classpath:"+resource);
            jsonString = new String(Files.readAllBytes(file.toPath()));
        } catch (FileNotFoundException e) {
            logger.info("FileNotFoundException: Щось пішло не так під час читання файлу переліку категорій для ITunes.");
            return null;
        } catch (IOException e) {
            logger.info("IOException: Щось пішло не так під час читання файлу переліку категорій для ITunes.");
            return null;
        }
        // отримали рядок з файлу
        return new Gson().fromJson(jsonString, new TypeToken<HashMap<String, Object>>() {}.getType());
    }

    public void SaveItunesCategory(PodcastItunesCategory pic) {
        itunesCategoryRepository.save(pic);
    }

    public void ItunesCategoryClear(PodcastItunesCategory toclear) {
        itunesCategoryRepository.delete(toclear);
    }
}
