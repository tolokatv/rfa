package media.toloka.rfa.radio.store;
// https://paulcwarren.github.io/spring-content/refs/release/1.2.4/fs-index.html


import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.creater.service.CreaterService;
import media.toloka.rfa.radio.dropfile.service.FilesService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.store.Service.StoreService;
import media.toloka.rfa.radio.store.model.Store;
import media.toloka.rfa.security.model.Users;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.apache.commons.io.IOUtils

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;

@Controller
public class StoreSiteController {

    final Logger logger = LoggerFactory.getLogger(StoreSiteController.class);

    @Autowired
    private ClientService clientService;

    @Autowired
    private FilesService filesService;

    @Autowired
    private CreaterService createrService;

    @Autowired
    private StoreService storeService;

    @GetMapping(value = "/store/audio/{clientUUID}/{fileName}",
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public @ResponseBody byte[] getStoreAudio(
            @PathVariable String clientUUID,
            @PathVariable String fileName,
            Model model) {
        Clientdetail cd = clientService.GetClientDetailByUuid(clientUUID);
//        http://localhost:8080/store/e2f9b0e6-73b5-4fcf-b249-f1e82d42a689/123.jpg
        // todo Прибрати роботу з ресурсами і зробити звичайну роботу з файлами.
        InputStream is = getClass()
                .getResourceAsStream("/upload/"+clientUUID+"/"+fileName);
        if (is == null) {
            return new byte[0];
        }
        try {
            byte[] buffer = is.readAllBytes();

            return buffer;
        } catch (IOException e) {
            logger.info("==================================== getStoreImage IOException");
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(value = "/store/img/{clientUUID}/{fileName}",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public @ResponseBody byte[] getStoreImage(
            @PathVariable String clientUUID,
            @PathVariable String fileName,
            Model model ) {
        Clientdetail cd = clientService.GetClientDetailByUuid(clientUUID);
//        http://localhost:8080/store/e2f9b0e6-73b5-4fcf-b249-f1e82d42a689/123.jpg
        // todo Прибрати роботу з ресурсами і зробити звичайну роботу з файлами.
        InputStream is = getClass()
                .getResourceAsStream("/upload/"+clientUUID+"/"+fileName);
        if (is == null) {
            return new byte[0];
        }
        try {
            byte[] buffer = is.readAllBytes();

            return buffer;
        } catch (IOException e) {
            logger.info("==================================== getStoreImage IOException");
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(value = "/store/thrumbal/{clientUUID}/{fileName}",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public @ResponseBody byte[] getStoreThrumbal(
            @PathVariable String clientUUID,
            @PathVariable String fileName,
            Model model ) {
        // https://medium.com/@asadise/create-thumbnail-for-an-image-in-spring-framework-49776c873ea1
        Clientdetail cd = clientService.GetClientDetailByUuid(clientUUID);
        // http://localhost:8080/store/thrumbal/e2f9b0e6-73b5-4fcf-b249-f1e82d42a689/123.jpg
        InputStream is = getClass()
                .getResourceAsStream("/upload/"+clientUUID+"/"+fileName);
        OutputStream os;

        BufferedImage thumbImg = null;
        BufferedImage img;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
             img = ImageIO.read(is);
        } catch (IOException e) {
            logger.info("==================================== getStoreImage IOException");
            e.printStackTrace();
            return null;
        }
        thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, 320, Scalr.OP_ANTIALIAS);
        try {
            ImageIO.write(thumbImg, FilenameUtils.getExtension(fileName), baos);
        } catch (IOException e) {
            logger.info("==================================== getStoreImage IOException");
            e.printStackTrace();
            return null;
        }
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    @GetMapping(value = "/creater/store/{pageNumber}",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public String getStoreList(
            @PathVariable int pageNumber,
//            @PathVariable String fileName,
//            @ModelAttribute Clientdetail fuserdetail,
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }
        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());

//        List<Store> fileList = storeService.GetAllByClientId(cd);
//        model.addAttribute("fileList", fileList );

        Page page = storeService.GetStorePageByClientDetail(pageNumber,10, cd);
        List<Store> storeList = page.stream().toList();

//        model.addAttribute("fileList", fileList );
        model.addAttribute("storeList", storeList );
        model.addAttribute("page", page );
//        model.addAttribute("docList", docList );
//        model.addAttribute("coverList", coverList );
//        model.addAttribute("trackList", trackList );
        return "/store/mainstore";
    }


}
