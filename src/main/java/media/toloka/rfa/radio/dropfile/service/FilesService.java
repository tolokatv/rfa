package media.toloka.rfa.radio.dropfile.service;

import media.toloka.rfa.model.Clientdetail;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.dropfile.DropPostFileController;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FilesService {

    @Autowired
    private ClientService clientService;

    @Value("${media.toloka.rfa.upload_directory}")
    private String PATH;

    final Logger logger = LoggerFactory.getLogger(DropPostFileController.class);

    public String GetClientDirectory() {
        Users usr = clientService.GetCurrentUser();
        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        String sss = cd.getUuid();
//        String str = clientService.getClientDetail(clientService.GetCurrentUser()).getUuid();
        return PATH.concat(sss);
    }
}
