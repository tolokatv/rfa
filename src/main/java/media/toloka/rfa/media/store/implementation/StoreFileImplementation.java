package media.toloka.rfa.media.store.implementation;

import media.toloka.rfa.media.store.StoreSiteController;
import media.toloka.rfa.media.store.model.EStoreFileType;
import media.toloka.rfa.radio.creater.service.CreaterService;
import media.toloka.rfa.radio.document.service.DocumentService;
import media.toloka.rfa.radio.dropfile.service.FilesService;
import media.toloka.rfa.media.store.Interface.StoreInterface;
import media.toloka.rfa.media.store.Reposirore.StoreRepositorePagination;
import media.toloka.rfa.media.store.Service.StoreService;
import media.toloka.rfa.media.store.model.Store;
import media.toloka.rfa.radio.history.service.HistoryService;
import media.toloka.rfa.radio.model.Clientdetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static media.toloka.rfa.media.store.model.EStoreFileType.STORE_DOCUMENT;
import static media.toloka.rfa.radio.model.enumerate.EHistoryType.History_DocumentCreate;

public class StoreFileImplementation implements StoreInterface {

    //@Value("${media.toloka.rfa.upload_directory}")
    //private String PATHuploadDirectory;

    @Autowired
    private StoreRepositorePagination storerepositore;

    @Autowired
    private StoreService storeService;

    @Autowired
    private FilesService filesService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private CreaterService createrService;

    @Autowired
    private HistoryService historyService;

    final Logger logger = LoggerFactory.getLogger(StoreFileImplementation.class);

    @Override
    public InputStream GetFileFromStore(String uuid) {
        Store store = storerepositore.getByUuid(uuid);
        File file = new File(store.getFilepatch());
        if (file.exists()) {
            try {
                InputStream is = new FileInputStream(file);
                return is;
            } catch (Exception ex) {
                logger.warn("======= Exception -> StoreFileImplementation GetFileFromStore FileNotFoundException");
                return null;
            }
        } else {
            return null;
        }
    }
    @Override
    public String PutFileToStore(InputStream inputStream, String filename, Clientdetail cd, EStoreFileType storeFileType) {
//        String newFilePatch = filesService.GetBaseClientDirectory(cd) + "/" + filesService.GetUploadDirectory() + "/" + filename;
        Path destination = Paths.get(filesService.GetBaseClientDirectory(cd) + "/" + filesService.GetUploadDirectory()).resolve(filename).normalize().toAbsolutePath();
        //Boolean fileExist = Files.exists(destination);
        Boolean fileExist = Files.exists(destination);

        try {
            Files.createDirectories(destination.getParent());
            Files.copy(inputStream, destination, REPLACE_EXISTING);
//            InputStream initialStream = new FileInputStream(new File(newFilePatch));
//            byte[] buffer = new byte[inputStream.available()];
//            inputStream.read(buffer);
//            Path destination = Paths.get(filesService.GetBaseClientDirectory(cd) + "/" + filesService.GetUploadDirectory())
//                    .resolve(filename).normalize().toAbsolutePath();
            // дивимося, чи є такий файл у сховищі
//            Store store11111 = storerepositore.getByFilepatch(destination.toString());
            // якщо є, то створюємо нову версію
//            OutputStream targetFileStream = new FileOutputStream(new File(destination.toString()));
//            targetFileStream.write(buffer);
//            targetFileStream.close();
//            Files.write(buffer, targetFileStream);
        } catch (IOException e) {
            logger.warn("IOExeption StoreFileImplementation PutFileToStore {} {}", cd.getUuid(), filename);
        }

        // Зберігаємо інформацію о файлі та привʼязуємо до користувача.
        Random random = new Random();
        long difference = random.nextInt(1000);  // затримка задля не повторення ID в базі
        Store storeitem;
        try {
            Thread.sleep(difference);
            if (!fileExist) {
                storeitem = storeService.SaveStoreItemInfo(null,destination, storeFileType, cd);
                switch (storeFileType) {
                    case STORE_DOCUMENT:
                        documentService.saveDocumentUploadInfo(destination);
                        break;
                    case STORE_TRACK:
                        createrService.SaveTrackUploadInfo(destination, storeitem, cd);
                        break;
                    //case
                }

            } else {
                storeitem = storeService.GetStoreItemByFilenameByClientDetail(destination.getFileName().toString(), cd);
            }
            historyService.saveHistory(History_DocumentCreate, " Завантажено файл: " + filename, cd.getUser());
        }
        catch(InterruptedException e)
        {
            logger.info("--------- Thread.sleep(difference) -> catch(InterruptedException e)");
        }









        Store store = new Store();
        store.setFilename(filename);
        store.setFilepatch(destination.toString());
        store.setClientdetail(cd);
        store.setFilelength(filesService.GetMediaLength(destination));
        store.setContentMimeType(filesService.GetMediatype(destination));
        storerepositore.save(store);
        return store.getUuid();

    }

    public Boolean DeleteFileInStore(String uuid) {
        return true;
    }

}
