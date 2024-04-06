package media.toloka.rfa.radio.creater.fileupload;

import lombok.extern.slf4j.Slf4j;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.creater.service.CreaterService;
import media.toloka.rfa.radio.document.service.DocumentService;
import media.toloka.rfa.radio.dropfile.service.FilesService;
import media.toloka.rfa.radio.history.service.HistoryService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.store.Service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static media.toloka.rfa.radio.store.model.EStoreFileType.STORE_ALBUMCOVER;
import static media.toloka.rfa.radio.store.model.EStoreFileType.STORE_TRACK;


@Slf4j
@RestController
//@RequestMapping("/uploadfile")
public class CreaterDropPostFileController {

    @Value("${media.toloka.rfa.upload_directory}")
    private String PATHuploadDirectory;

    @Autowired
    private ClientService clientService;
    @Autowired
    private DocumentService documentService;

    @Autowired
    private CreaterService createrService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private FilesService filesService;

    @Autowired
    private StoreService storeService;

    final Logger logger = LoggerFactory.getLogger(CreaterDropPostFileController.class);

    @PostMapping(path = "/creater/trackupload" ) // , produces = MediaType.APPLICATION_JSON_VALUE
    public void uploadTrack(@RequestParam("file") MultipartFile file) {

//        log.info("uploaded file " + file.getOriginalFilename());
        if (file.isEmpty()) {
//                throw new ExecutionControl.UserException("Empty file");
            logger.info("Завантаження файлу: Файл порожній");
        }
        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        if (clientService.ClientCanDownloadFile(cd) == false) {
            // клієнт з якоїсь причини не має права завантажувати файли
            logger.warn("Клієнт {} не має права завантажувати файли.", cd.getUuid());
            return;
        }
        try {
            // тестуємо завантаження через сервіси сховища.
            String storeUUID = storeService.PutFileToStore(file.getInputStream(),file.getOriginalFilename(),cd,STORE_TRACK);
        } catch (IOException e) {
            logger.info("Завантаження файлу: Проблема збереження");
            e.printStackTrace();
        }
        log.info("uploaded file " + file.getOriginalFilename());

    }

    @PostMapping(path = "/creater/albumcoverupload" ) // , produces = MediaType.APPLICATION_JSON_VALUE
    public void uploadAlbumCover(@RequestParam("file") MultipartFile file) {

        log.info("Завантажуємо обкладинку " + file.getOriginalFilename());
        if (file.isEmpty()) {
//                throw new ExecutionControl.UserException("Empty file");
            logger.info("Завантаження файлу: Файл порожній");
        }
        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        if (clientService.ClientCanDownloadFile(cd) == false) {
            // клієнт з якоїсь причини не має права завантажувати файли
            logger.warn("Клієнт {} не має права завантажувати файли.", cd.getUuid());
            return;
        }

        try {
            // тестуємо завантаження через сервіси сховища.
            String storeUUID = storeService.PutFileToStore(file.getInputStream(),file.getOriginalFilename(),cd,STORE_ALBUMCOVER);
            // todo додати storeUUID до альбому
        } catch (IOException e) {
            logger.info("Завантаження файлу: Проблема збереження");
            e.printStackTrace();
        }
        log.info("uploaded file " + file.getOriginalFilename());

    }

    // Старі версії.

//        @PostMapping(path = "/creater/trackupload-old" ) // , produces = MediaType.APPLICATION_JSON_VALUE
//    public void uploadTrack_Old(@RequestParam("file") MultipartFile file) {
//
////        log.info("uploaded file " + file.getOriginalFilename());
//        if (file.isEmpty()) {
////                throw new ExecutionControl.UserException("Empty file");
//            logger.info("Завантаження файлу: Файл порожній");
//        }
//        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
//        if (clientService.ClientCanDownloadFile(cd) == false) {
//            // клієнт з якоїсь причини не має права завантажувати файли
//            logger.warn("Клієнт {} не має права завантажувати файли.",cd.getUuid());
//            return;
//        }
//
//        // приводимо завантаження файлів до одного каталогу
//        // HOME media.toloka.rfa.server.client_dir client.UUID upload
//        // Path destination = Paths.get(filesService.GetClientDirectory(cd)).resolve(file.getOriginalFilename()).normalize().toAbsolutePath();
//        //String pathConfigFile = System.getenv("HOME") + cd.getUuid() + "/" + station.getClientdetail().getUuid() + "/"
//        //        + station.getUuid() + "/" + station.getDbname() + ".rfa.toloka.media";
//        Path destination = Paths.get(filesService.GetBaseClientDirectory(cd)).resolve(file.getOriginalFilename()).normalize().toAbsolutePath();
//        Boolean fileExist = Files.exists(destination);
//        String mediatype =  filesService.GetMediatype(destination);
//        try {
//            Files.createDirectories(destination.getParent());
//            Files.copy(file.getInputStream(), destination, REPLACE_EXISTING);
//            // Зберігаємо інформацію о файлі та привʼязуємо до користувача.
//            Random random = new Random();
//            long difference = random.nextInt(1000);
////            logger.info("Завантаження файлу: Випадкова затримка {}",difference);
//            Store storeitem;
//            try {
//                Thread.sleep(difference);
//                if (!fileExist) { // Зберігаємо новий файл, якого не було у каталозі для завантаження
//                    storeitem = storeService.SaveStoreItemInfo(null,destination, STORE_TRACK, cd);
//                    createrService.SaveTrackUploadInfo(destination, storeitem, cd);
//                } else { // файл з таким імʼям був в каталозі. Оновлюємо інформацію
//                    storeitem = storeService.GetStoreItemByFilenameByClientDetail(destination.getFileName().toString(), cd);
//                }
//                historyService.saveHistory(History_DocumentCreate, " Завантажено трек: " + file.getOriginalFilename(), clientService.GetCurrentUser());
//            }
//            catch(InterruptedException e)
//            {
//                logger.info("--------- catch(InterruptedException e)");
//            }
//        } catch (IOException e) {
//            logger.info("Завантаження треку: Проблема збереження");
//            e.printStackTrace();
//        }
//        log.info("uploaded track " + file.getOriginalFilename());
//    }
//
//    @PostMapping(path = "/creater/albumcoverupload-old" ) // , produces = MediaType.APPLICATION_JSON_VALUE
//    public void uploadAlbumCover_OLD(@RequestParam("file") MultipartFile file) {
//
//        log.info("Завантажуємо обкладинку " + file.getOriginalFilename());
//        if (file.isEmpty()) {
////                throw new ExecutionControl.UserException("Empty file");
//            logger.info("Завантаження файлу: Файл порожній");
//        }
//        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
//        if (clientService.ClientCanDownloadFile(cd) == false) {
//            // клієнт з якоїсь причини не має права завантажувати файли
//            logger.warn("Клієнт {} не має права завантажувати файли.",cd.getUuid());
//            return;
//        }
//
//        Path destination = Paths.get(filesService.GetBaseClientDirectory(cd)).resolve(file.getOriginalFilename()).normalize().toAbsolutePath();
//        Boolean fileExist = Files.exists(destination);
//        Store storeitem = null;
//        try {
//            Files.createDirectories(destination.getParent());
//            Files.copy(file.getInputStream(), destination, REPLACE_EXISTING);
//
//
//            try {
//                // Зберігаємо інформацію о файлі та привʼязуємо до користувача.
//                Random random = new Random();
//                long difference = random.nextInt(1000);
////                logger.info("Завантаження файлу: Випадкова затримка {}",difference);
//                Thread.sleep(difference);
//
//                if (fileExist) {
////                    storeitem = storeService.SaveStoreItemInfo(null,destination, STORE_ALBUMCOVER, cd);                    createrService.SaveAlbumCoverUploadInfo(destination, cd, storeitem);
////                } else {
//                    storeitem = storeService.GetStoreItemByFilenameByClientDetail(destination.getFileName().toString(), cd);
////                    storeitem = storeService.SaveStoreItemInfo(storeitem,destination, STORE_ALBUMCOVER, cd);
//                }
//                storeitem = storeService.SaveStoreItemInfo(storeitem,destination, STORE_ALBUMCOVER, cd);
//                historyService.saveHistory(History_DocumentCreate, " Завантажено обкладинку альбому: " + file.getOriginalFilename(), clientService.GetCurrentUser());
//            }
//            catch(InterruptedException e)
//            {
//                logger.info("--------- catch(InterruptedException e)");
//            }
//        } catch (IOException e) {
//            logger.info("Завантаження треку: Проблема збереження");
////            e.printStackTrace();
//        }
//        log.info("uploaded track " + file.getOriginalFilename());
//    }

}

