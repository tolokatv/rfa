package media.toloka.rfa.media.store.implementation;

import media.toloka.rfa.radio.dropfile.service.FilesService;
import media.toloka.rfa.media.store.Interface.StoreInterface;
import media.toloka.rfa.media.store.Reposirore.StoreRepositorePagination;
import media.toloka.rfa.media.store.Service.StoreService;
import media.toloka.rfa.media.store.model.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;

public class StoreFileImplementation implements StoreInterface {

    @Value("${media.toloka.rfa.upload_directory}")
    private String PATHuploadDirectory;

    @Autowired
    private StoreRepositorePagination storerepositore;

    @Autowired
    private StoreService storeService;

    @Autowired
    private FilesService filesService;

    @Override
    public InputStream GetFileFromStore (String uuid) {
        Store store = storerepositore.getByUuid(uuid);
//        File file = new File(store.getFilepatch());
//        if (file.exists()) {
//            InputStream is =  new FileInputStream(file);
//            return is;
//        }
            return null;
    }

    public String PutFileToStore (InputStream inputStream, String filename)
         {
            Store store = storerepositore.getByFilepatch(filename);
//            InputStream initialStream = new FileInputStream(
//                    new File(store.getFilepatch()));
//            byte[] buffer = new byte[inputStream.available()];
//            inputStream.read(buffer);
//            Path destination = Paths.get(filesService.GetClientDirectory()).resolve(filename).normalize().toAbsolutePath();
//            // дивимося, чи є такий файл у сховищі
//            Store store = storerepositore.getByFilepatch(destination.toString());
//            // якщо є, то створюємо нову версію
//            OutputStream targetFile = new File(destination);
//            Files.write(buffer, targetFile);
            return store.getUuid();

    }

    public Boolean DeleteFileInStore (String uuid) {
        return true;
    }

}
