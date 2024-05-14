package media.toloka.rfa.radio.podcast.fileupload;

import lombok.extern.slf4j.Slf4j;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.creater.service.CreaterService;
import media.toloka.rfa.radio.document.service.DocumentService;
import media.toloka.rfa.radio.dropfile.service.FilesService;
import media.toloka.rfa.radio.history.service.HistoryService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.podcast.model.PodcastChannel;
import media.toloka.rfa.radio.podcast.service.PodcastService;
import media.toloka.rfa.radio.store.Service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static media.toloka.rfa.radio.store.model.EStoreFileType.*;


@Slf4j
@RestController
//@RequestMapping("/uploadfile")
public class PodcastDropPostFileController {

    @Value("${media.toloka.rfa.upload_directory}")
    private String PATHuploadDirectory;

    @Autowired
    private ClientService clientService;

    @Autowired
    private PodcastService podcastService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private FilesService filesService;

    @Autowired
    private StoreService storeService;

    final Logger logger = LoggerFactory.getLogger(PodcastDropPostFileController.class);

    @PostMapping(path = "/podcast/episodeupload/{puuid}" ) // , produces = MediaType.APPLICATION_JSON_VALUE
    public void EpisodeUpload(
            @PathVariable String puuid,
            @RequestParam("file") MultipartFile file) {

//        log.info("uploaded file " + file.getOriginalFilename());
        if (file.isEmpty()) {
//                throw new ExecutionControl.UserException("Empty file");
            logger.info("Завантаження епізоду подкасту: Файл порожній");
        }
        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        if (clientService.ClientCanDownloadFile(cd) == false) {
            // клієнт з якоїсь причини не має права завантажувати файли
            logger.warn("Клієнт {} не має права завантажувати файли.", cd.getUuid());
            return;
        }
        PodcastChannel podcast = podcastService.GetChanelByUUID(puuid);
        log.info("Current episode {} {}",puuid, podcast.getTitle());
        try {
            String storeUUID = storeService.PutFileToStore(file.getInputStream(),file.getOriginalFilename(),cd,STORE_EPISODETRACK);
            podcastService.SaveEpisode(storeUUID, podcast, cd);
        } catch (IOException e) {
            logger.info("Завантаження файлу: Проблема збереження");
            e.printStackTrace();
        }
        log.info("uploaded file " + file.getOriginalFilename());

    }

    // Старі версії.


}

