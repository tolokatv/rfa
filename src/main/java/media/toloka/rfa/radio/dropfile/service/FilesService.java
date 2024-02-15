package media.toloka.rfa.radio.dropfile.service;

import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.dropfile.DropPostFileController;
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

import static org.apache.commons.io.FilenameUtils.getExtension;

@Service
public class FilesService {

    @Autowired
    private ClientService clientService;

    @Value("${media.toloka.rfa.upload_directory}")
    private String PATHuploadDirectory;

    final Logger logger = LoggerFactory.getLogger(FilesService.class);

    public String GetClientDirectory(Clientdetail cd) {
//        Users usr = clientService.GetCurrentUser();
//        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        String clientUuid = cd.getUuid();
//        String str = clientService.getClientDetail(clientService.GetCurrentUser()).getUuid();
        return PATHuploadDirectory.concat(clientUuid);
    }

//    public String GetFileLink(String userUUID, String filename) {
//        Path temp = Paths.get(userUUID);
////        Set<String> stringSet = listFilesUsingDirectoryStream(temp.toString());
//        return temp.toString();
//    }

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
