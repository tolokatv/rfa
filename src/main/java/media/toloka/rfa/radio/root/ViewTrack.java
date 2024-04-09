package media.toloka.rfa.radio.root;

import media.toloka.rfa.radio.creater.service.CreaterService;
import media.toloka.rfa.radio.model.Track;
import media.toloka.rfa.radio.store.model.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@Controller
public class ViewTrack {

    @Autowired
    private CreaterService createrService;

    @GetMapping(value = "/guest/viewtrack/{trackuuid}")
    public String getTracksAll(
            @PathVariable String trackuuid,
            Model model) {

        Track curtrack = createrService.GetTrackByUuid(trackuuid);
        model.addAttribute("curtrack", curtrack );
        return "/guest/viewtrack";
    }

}
