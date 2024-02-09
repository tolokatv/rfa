package media.toloka.rfa.radio.dropfile.service;

import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.dropfile.DropPostFileController;
import media.toloka.rfa.radio.model.Track;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Value("${media.toloka.rfa.upload_directory}")
    private String PATH;

    final Logger logger = LoggerFactory.getLogger(DropPostFileController.class);

    public String GetClientDirectory() {
//        Users usr = clientService.GetCurrentUser();
        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        String sss = cd.getUuid();
//        String str = clientService.getClientDetail(clientService.GetCurrentUser()).getUuid();
        return PATH.concat(sss);
    }

    public String GetFileLink(String userUUID, String filename) {
        Path temp = Paths.get(userUUID);
//        Set<String> stringSet = listFilesUsingDirectoryStream(temp.toString());
        return temp.toString();
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

}
