package media.toloka.rfa.radio.store.implementation;

import media.toloka.rfa.radio.dropfile.service.FilesService;
import media.toloka.rfa.radio.store.Interface.StoreInterface;
import media.toloka.rfa.radio.store.Reposirore.StoreRepositorePagination;
import media.toloka.rfa.radio.store.Service.StoreService;
import media.toloka.rfa.radio.store.model.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
