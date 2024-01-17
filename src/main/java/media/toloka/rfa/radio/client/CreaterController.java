package media.toloka.rfa.radio.client;

import media.toloka.rfa.radio.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CreaterController {

    @Autowired
    private ClientService clientService;

    @GetMapping(value = "/creater/home")
    public String getUserHomeInfo(
            Model model ) {
        return "/creater/home";
    }
}
