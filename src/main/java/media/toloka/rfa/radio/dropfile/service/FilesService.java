package media.toloka.rfa.radio.dropfile.service;

import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.dropfile.DropPostFileController;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

@Service
public class FilesService {

    @Autowired
    private ClientService clientService;

    @Value("${media.toloka.rfa.upload_directory}")
    private String PATH;

    final Logger logger = LoggerFactory.getLogger(DropPostFileController.class);

    public String CheckClientDirectory() {
        return PATH.concat(clientService.getClientDetail(clientService.GetCurrentUser()).getUuid());
    }
}
