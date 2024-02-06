package media.toloka.rfa.radio.creater;

import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.creater.service.CreaterService;
import media.toloka.rfa.radio.document.ClientDocumentEditController;
import media.toloka.rfa.radio.model.Album;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CreaterAlbumController {

    final Logger logger = LoggerFactory.getLogger(CreaterAlbumController.class);

    @Autowired
    private CreaterService createrService;

    @Autowired
    private ClientService clientService;

    @GetMapping(value = "/creater/albums")
    public String getCreaterTracks(
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }
        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        List<Album> albumList = createrService.GetAllAlbumsByCreater(cd);
        model.addAttribute("albumList", albumList );
        return "/creater/albums";
    }
}
