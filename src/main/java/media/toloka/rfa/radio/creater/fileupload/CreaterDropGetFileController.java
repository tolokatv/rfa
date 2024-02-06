package media.toloka.rfa.radio.creater.fileupload;

import lombok.extern.slf4j.Slf4j;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Slf4j
@Controller
@RequestMapping("/creator/uploadtrack")
public class CreaterDropGetFileController {

    @Autowired
    private ClientService clientService;
    final Logger logger = LoggerFactory.getLogger(CreaterDropGetFileController.class);

    @GetMapping
    public String GetUploadFile() {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }
        return "/creater/fileupload";
    }
}

