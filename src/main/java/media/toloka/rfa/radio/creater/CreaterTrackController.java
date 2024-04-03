package media.toloka.rfa.radio.creater;


import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.creater.service.CreaterService;
import media.toloka.rfa.radio.document.ClientDocumentEditController;
import media.toloka.rfa.radio.dropfile.service.FilesService;
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
public class CreaterTrackController {

    final Logger logger = LoggerFactory.getLogger(ClientDocumentEditController.class);

    @Autowired
    private ClientService clientService;

    @Autowired
    private FilesService filesService;
    @Autowired
    private CreaterService createrService;

    @GetMapping(value = "/creater/tracks")
    public String getCreaterTracks(
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }
        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        List<Track> trackList = createrService.GetAllTracksByCreater(cd);
        List<Album> albums = createrService.GetAllAlbumsByCreater(cd);

        String baseaddress = filesService.GetBaseClientDirectory(cd);
        model.addAttribute("baseaddress", baseaddress );
        model.addAttribute("albums", albums );
        model.addAttribute("trackList", trackList );
        return "/creater/tracks";
    }

    // /creater/edittrack/'+${track.id}
    @GetMapping(value = "/creater/edittrack/{idTrack}")
    public String getCreaterEditTracks(
            @PathVariable Long idTrack,
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }
        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        Track track = createrService.GetTrackById(idTrack);

        List<Album> albumList = createrService.GetAllAlbumsByCreater(cd);

        model.addAttribute("albumList", albumList );
        model.addAttribute("track", track );
        return "/creater/edittrack";
    }
    @PostMapping(value = "/creater/edittrack")
    public String getCreaterEditTracks(
//            @PathVariable Long idTrack,
            @ModelAttribute Track ftrack,
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }
        Clientdetail cd = clientService.GetClientDetailByUser(user);
        Track track = createrService.GetTrackById(ftrack.getId());
        if (track == null) {
            logger.info("З якогось дива не знайшли трек {}", ftrack.getId());
            return "/creater/home";
        }
        track.setName(ftrack.getName());
        track.setDescription(ftrack.getDescription());
        track.setNotnormalvocabulary(ftrack.getNotnormalvocabulary());
        track.setStyle(ftrack.getStyle());
        track.setAutor(ftrack.getAutor());
        if (track.getTochat() != ftrack.getTochat()) {
            track.setTochat(ftrack.getTochat());

            createrService.PublicTrackToChat(track, cd );
        }
        if (ftrack.getAlbum() != null) {
            track.setAlbum(ftrack.getAlbum());
        } else {
            track.setAlbum(null);
        }

        createrService.SaveTrack(track);

        List<Track> trackList = createrService.GetAllTracksByCreater(cd);
        model.addAttribute("trackList", trackList );
        return "/creater/tracks";
    }


}
