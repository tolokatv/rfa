package media.toloka.rfa.radio.dropfile.service;

import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.client.service.ClientService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Service
public class FilesService {

    @Autowired
    private ClientService clientService;

    @Value("${media.toloka.rfa.server.client_dir}")
    private String clientdir;
    @Value("${media.toloka.rfa.upload_directory}")
    private String PATHuploadDirectory;

    final Logger logger = LoggerFactory.getLogger(FilesService.class);

    public String GetBaseClientDirectory(Clientdetail cd) {
        // Базова клієнтська директорія Тут знаходяться завантажені файли та каталоги радіостанцій.
        String clientDirectory = System.getenv("HOME") + clientdir + "/" + cd.getUuid() ;
        return clientDirectory;
    }

    public String GetUploadDirectory () {
        return PATHuploadDirectory;
    }

    // перенести в StoreInterface
    public String GetMediatype(Path fileName) {
        String mediaType;
        String extensionFile =  FilenameUtils.getExtension(fileName.getFileName().toString());
        try {
            mediaType = Files.probeContentType(fileName);
        } catch (IOException e) {
            logger.info("GetMediatype:  Йой!");
            return null;
        }
        return mediaType;
    }

    public Set<String> listFilesUsingDirectoryStream(String dir) throws IOException {
        Set<String> fileSet = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileSet.add(path.getFileName()
                            .toString());
                }
            }
        }
        return fileSet;
    }

    public Long GetMediaLength(Path destination) {
//        Long fileSize;
//        File storeFile = new File(destination);
        Long size = FileUtils.sizeOf(new File(destination.toString()));
        return size;
    }
}
