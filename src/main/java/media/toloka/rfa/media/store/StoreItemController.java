package media.toloka.rfa.media.store;


import media.toloka.rfa.media.store.Service.StoreService;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.creater.service.CreaterService;
import media.toloka.rfa.radio.dropfile.service.FilesService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StoreItemController {

    final Logger logger = LoggerFactory.getLogger(StoreItemController.class);

    @Autowired
    private ClientService clientService;

    @Autowired
    private FilesService filesService;

    @Autowired
    private CreaterService createrService;

    @Autowired
    private StoreService storeService;

//    http://localhost:8080/creater/edititem/452
    @GetMapping(value = "/store/edititem/{storeItemId}")
    public @ResponseBody String GetStoreItemEdit(
        @PathVariable Long storeItemId,
        Model model) {
        Users user = clientService.GetCurrentUser();
        if (user == null) { return "redirect:/"; }

        Clientdetail cd = clientService.GetClientDetailByUser(user);
        return "/store/edititem";
    }

    @PostMapping(value = "/store/deleteitem/{storeItemId}")
    public @ResponseBody String PostStoreItemDelete(
            @PathVariable Long storeItemId,
            Model model) {

        Users user = clientService.GetCurrentUser();
        if (user == null) { return "redirect:/"; }

        Clientdetail cd = clientService.GetClientDetailByUser(user);


//        model.addAttribute("store", store );
        return "/store/edititem";

    }
}
