package media.toloka.rfa.radio.creater;

import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.creater.service.CreaterService;
import media.toloka.rfa.radio.document.ClientDocumentEditController;
import media.toloka.rfa.radio.model.Album;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Track;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CreaterAlbumController {

    final Logger logger = LoggerFactory.getLogger(CreaterAlbumController.class);

    @Autowired
    private CreaterService createrService;

    @Autowired
    private ClientService clientService;

    @GetMapping(value = "/creater/albums")
    public String getCreaterAlbums(
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

    @GetMapping(value = "/creater/editalbum/{idAlbum}")
    public String getCreaterAlbums(
            @PathVariable Long idAlbum,
            @ModelAttribute Album falbum,
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }

        Clientdetail cd = clientService.GetClientDetailByUser(user);
        Album album;
        if (idAlbum == 0L) {
            album = new Album();
            album.setClientdetail(cd);
            createrService.SaveAlbum(album);
        } else {
            album = createrService.GetAlbumById(idAlbum);
        }


//        List<Album> albumList = createrService.GetAllAlbumsByCreater(cd);
//        model.addAttribute("albumList", albumList );
        model.addAttribute("album", album );
        return "/creater/editalbum";
    }

    @PostMapping(value = "/creater/editalbum")
    public String getCreaterEditTracks(
//            @PathVariable Long idAlbum,
            @ModelAttribute Album falbum,
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }
        Clientdetail cd = clientService.GetClientDetailByUser(user);
        Album album = createrService.GetAlbumById(falbum.getId());
        if (album == null) {
            logger.info("З якогось дива не знайшли альбом {}", falbum.getId());
            return "/creater/home";
        }
        album.setName(falbum.getName());
        album.setDescription(falbum.getDescription());
        album.setAlbumrelisedate(falbum.getAlbumrelisedate());
        album.setStyle(falbum.getStyle());
        album.setAutor(falbum.getAutor());
        if (album.getClientdetail() == null) { album.setClientdetail(cd); }

        createrService.SaveAlbum(album);

        List<Album> albumList = createrService.GetAllAlbumsByCreater(cd);
        model.addAttribute("albumList", albumList );
        return "/creater/albums";
    }


}
