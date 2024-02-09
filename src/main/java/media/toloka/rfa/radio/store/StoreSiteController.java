package media.toloka.rfa.radio.store;


import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.creater.service.CreaterService;
import media.toloka.rfa.radio.dropfile.service.FilesService;
import media.toloka.rfa.radio.model.Clientdetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.apache.commons.io.IOUtils

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

//    @Autowired
//    private ClientService clientService;

    @GetMapping(value = "/store/img/{clientUUID}/{fileName}",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public @ResponseBody byte[] getStoreImage(
            @PathVariable String clientUUID,
            @PathVariable String fileName,
            Model model ) {
        Clientdetail cd = clientService.GetClientDetailByUuid(clientUUID);
//        http://localhost:8080/store/e2f9b0e6-73b5-4fcf-b249-f1e82d42a689/123.jpg
        InputStream is = getClass()
                .getResourceAsStream("/upload/"+clientUUID+"/"+fileName);

        try {
            return is.readAllBytes();
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
//        http://localhost:8080/store/thrumbal/e2f9b0e6-73b5-4fcf-b249-f1e82d42a689/123.jpg
        InputStream is = getClass()
                .getResourceAsStream("/upload/"+clientUUID+"/"+fileName);
//        ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();
//        BufferedImage thumbImg = null;
//        BufferedImage img = ImageIO.read(is);
//        thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, width, Scalr.OP_ANTIALIAS);


        try {
            return is.readAllBytes();
        } catch (IOException e) {
            logger.info("==================================== getStoreImage IOException");
            e.printStackTrace();
            return null;
        }
    }



}
