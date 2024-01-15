package media.toloka.rfa.radio.dropfile;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
@RequestMapping("/uploadfile")

public class DropGetFileController {

    final Logger logger = LoggerFactory.getLogger(DropGetFileController.class);
    @GetMapping
    public String GetUploadFile() {
        logger.info("Стартуємо завантаження файлу");
        return "/fileupload";
    }
}

