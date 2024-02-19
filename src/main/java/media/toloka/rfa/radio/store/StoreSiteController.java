package media.toloka.rfa.radio.store;
// https://paulcwarren.github.io/spring-content/refs/release/1.2.4/fs-index.html


import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.creater.service.CreaterService;
import media.toloka.rfa.radio.dropfile.service.FilesService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.store.Interface.StoreInterface;
import media.toloka.rfa.radio.store.Service.StoreService;
import media.toloka.rfa.radio.store.model.Store;
import media.toloka.rfa.security.model.Users;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.apache.commons.io.IOUtils

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.imgscalr.Scalr;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.imageio.ImageIO;

import static org.springframework.http.HttpHeaders.ACCEPT_RANGES;

@Controller
public class StoreSiteController  {

    final Logger logger = LoggerFactory.getLogger(StoreSiteController.class);

    @Autowired
    private ClientService clientService;

    @Autowired
    private FilesService filesService;

    @Autowired
    private CreaterService createrService;

    @Autowired
    private StoreService storeService;

    @GetMapping(value = "/store/taudio/{clientUUID}/{fileName}",
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<StreamingResponseBody> stream(
            @PathVariable String clientUUID,
            @PathVariable String fileName,
            Model model) throws IOException {
        Clientdetail cd = clientService.GetClientDetailByUuid(clientUUID);
        String ifile = filesService.GetClientDirectory(cd)+"/"+fileName;
        String mymetype = filesService.GetMediatype(Paths.get(ifile));
        Long length = filesService.GetMediaLength(Paths.get(ifile));


        //This is just a sample to for creating the input stream as it's what I get from google cloud storage
        File file = new File(ifile);
        FileInputStream in = FileUtils.openInputStream(file);

        StreamingResponseBody stream = out -> {
            IOUtils.copy(in, out);
        };

        return ResponseEntity.ok()
                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .header(ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_TYPE, mymetype)
                .contentLength(length)
                .body(stream);
    }

    @GetMapping(value = "/store/audio/{clientUUID}/{fileName}",
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public @ResponseBody byte[] getStoreAudio(
            @PathVariable String clientUUID,
            @PathVariable String fileName,
            Model model) {
        Clientdetail cd = clientService.GetClientDetailByUuid(clientUUID);
//        http://localhost:8080/store/e2f9b0e6-73b5-4fcf-b249-f1e82d42a689/123.jpg
        // todo Прибрати роботу з ресурсами і зробити звичайну роботу з файлами.
        String ifile = filesService.GetClientDirectory(cd)+"/"+fileName;
//        logger.info("SCD = {}",ifile);
//        InputStream is = getClass().getResourceAsStream("/upload/"+clientUUID+"/"+fileName);
        InputStream is;
        try {
            is = new FileInputStream(new File(ifile));
            if (is == null) {
                return new byte[0];
            }
            byte[] buffer = is.readAllBytes();
            return buffer;
        } catch (FileNotFoundException e) {
            logger.info("getStoreAudio: Йой! FileNotFoundException!");
        } catch (IOException e) {
            logger.info("==================================== getStoreImage IOException");
            e.printStackTrace();
            return null;
        }
        return null;
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
        String ifile = filesService.GetClientDirectory(cd)+"/"+fileName;
//        logger.info("SCD = {}",ifile);
//        InputStream is = getClass().getResourceAsStream("/upload/"+clientUUID+"/"+fileName);
        InputStream is;
        try {
            is = new FileInputStream(new File(ifile));
            if (is == null) {
                return new byte[0];
            }
            byte[] buffer = is.readAllBytes();
            return buffer;
        } catch (FileNotFoundException e) {
            logger.info("getStoreAudio: Йой! FileNotFoundException!");
        } catch (IOException e) {
            logger.info("==================================== getStoreImage IOException");
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @GetMapping(value = "/store/thrumbal/{clientUUID}/{fileName}",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public @ResponseBody byte[] getStoreThrumbal(
            @PathVariable String clientUUID,
            @PathVariable String fileName,
            Model model ) {
        // https://medium.com/@asadise/create-thumbnail-for-an-image-in-spring-framework-49776c873ea1
        // http://localhost:8080/store/thrumbal/e2f9b0e6-73b5-4fcf-b249-f1e82d42a689/123.jpg
        Clientdetail cd = clientService.GetClientDetailByUuid(clientUUID);
//        http://localhost:8080/store/e2f9b0e6-73b5-4fcf-b249-f1e82d42a689/123.jpg
        // todo Прибрати роботу з ресурсами і зробити звичайну роботу з файлами.
        String ifile = filesService.GetClientDirectory(cd)+"/"+fileName;
//        logger.info("SCD = {}",ifile);
//        InputStream is = getClass().getResourceAsStream("/upload/"+clientUUID+"/"+fileName);
        InputStream is;

        OutputStream os;

        BufferedImage thumbImg = null;
        BufferedImage img;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            is = new FileInputStream(new File(ifile));
            if (is == null) {
                return new byte[0];
            }
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

    @GetMapping(value = "/store/thrumbal/w/{width}/{clientUUID}/{fileName}",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public @ResponseBody byte[] getStoreThrumbalWidth(
            @PathVariable String clientUUID,
            @PathVariable int width,
            @PathVariable String fileName,
            Model model ) {
        // https://medium.com/@asadise/create-thumbnail-for-an-image-in-spring-framework-49776c873ea1
        // http://localhost:8080/store/thrumbal/e2f9b0e6-73b5-4fcf-b249-f1e82d42a689/123.jpg
        Clientdetail cd = clientService.GetClientDetailByUuid(clientUUID);
//        http://localhost:8080/store/e2f9b0e6-73b5-4fcf-b249-f1e82d42a689/123.jpg
        // todo Прибрати роботу з ресурсами і зробити звичайну роботу з файлами.
        String ifile = filesService.GetClientDirectory(cd)+"/"+fileName;
//        logger.info("SCD = {}",ifile);
//        InputStream is = getClass().getResourceAsStream("/upload/"+clientUUID+"/"+fileName);
        InputStream is;

        OutputStream os;

        BufferedImage thumbImg = null;
        BufferedImage img;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            is = new FileInputStream(new File(ifile));
            if (is == null) {
                return new byte[0];
            }
            img = ImageIO.read(is);
        } catch (IOException e) {
            logger.info("==================================== getStoreImage IOException");
            e.printStackTrace();
            return null;
        }
        thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, width, Scalr.OP_ANTIALIAS);
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



}
