package media.toloka.rfa.radio.creater.service;


import media.toloka.rfa.radio.creater.repository.AlbumCoverRepository;
import media.toloka.rfa.radio.creater.repository.AlbumRepository;
import media.toloka.rfa.radio.creater.repository.TrackRepository;
import media.toloka.rfa.radio.document.ClientDocumentEditController;
import media.toloka.rfa.radio.dropfile.service.FilesService;
import media.toloka.rfa.radio.messanger.model.ChatMessage;
import media.toloka.rfa.radio.messanger.model.enumerate.EChatRecordType;
import media.toloka.rfa.radio.messanger.service.MessangerService;
import media.toloka.rfa.radio.model.*;
import media.toloka.rfa.radio.model.enumerate.EDocumentStatus;
import media.toloka.rfa.radio.post.repositore.PostRepositore;
import media.toloka.rfa.radio.store.model.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public class CreaterService {

    final Logger logger = LoggerFactory.getLogger(ClientDocumentEditController.class);

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private AlbumCoverRepository albumCoverRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private PostRepositore postRepositore;

    @Autowired
    private FilesService filesService;

    @Autowired
    private MessangerService messangerService;

    private SimpMessagingTemplate template;

    @Value("${media.toloka.rfa.server.chat.mainroom}")
    private String chatmainroom;
    @Value("${media.toloka.rfa.server.chat.trackroom}")
    private String chattrackroom;

    @Autowired
    public CreaterService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public List<Album> GetAllAlbumsByCreater(Clientdetail cd) {
        List<Album> albumList = albumRepository.findByClientdetail(cd);
        return albumList;
    }

    public List<Albumсover> GetAlbumCoverByCd (Clientdetail cd) {
        List<Albumсover> albumсoverList = albumCoverRepository.findByClientdetail(cd);
        return albumсoverList;
    }

    public List<Track> GetAllTracksByCreater(Clientdetail cd) {
        List<Track> trackList = trackRepository.findByClientdetail(cd);
        return trackList;
    }

    public List<Post> GetAllPostsByCreater(Clientdetail cd) {
        List<Post> postList = postRepositore.findByClientdetail(cd);
        return postList;
    }

    public List<Post> GetAllPostsByApruve(Boolean flag) {
        List<Post> postList = postRepositore.findByApruve( flag);
        return postList;
    }

    public void SaveTrackUploadInfo(Path destination, Store storeitem, Clientdetail cd) {
        Track track = new Track();
        track.setStatus(EDocumentStatus.STATUS_LOADED);
        track.setFilename(destination.getFileName().toString());
        track.setPatch(destination.toString());
        track.setClientdetail(cd);
        track.setStoreitem(storeitem);
        trackRepository.save(track);
    }

    public Track GetTrackById(Long idTrack) {
        return trackRepository.getById(idTrack);
    }

    public void SaveTrack(Track track) {
        trackRepository.save(track);
    }

    public Album GetAlbumById(Long id) {
        Album album = albumRepository.getById(id);
        return album;
    }

    public void SaveAlbum(Album album) {
        albumRepository.save(album);
    }

    public void SaveAlbumCoverUploadInfo(Path destination, Clientdetail cd, Store store) {
        Albumсover albumсover = new Albumсover();
        albumсover.setAlbumcoverfile(destination.getFileName().toString());
        String mediatype = filesService.GetMediatype(destination);
//        = Optional.ofNullable(destination.getFileName().toString())
//                .filter(f -> f.contains("."))
//                .map(f -> f.substring(destination.getFileName().toString().lastIndexOf(".") + 1));
        albumсover.setPatch(destination.toString());
        albumсover.setClientdetail(cd);
        albumсover.setStoreitem(store);
        albumCoverRepository.save(albumсover);
    }

    public List<Track> GetLastUploadTracks() {
//        return trackRepository.findAllByOrderByUploaddateAsc();
        return trackRepository.findAllTop10ByOrderByUploaddateAsc();
    }

    public Page GetTrackPage(int pageNumber, int pageCount) {
        Pageable storePage = PageRequest.of(pageNumber, pageCount);
        Page page = trackRepository.findAllByOrderByUploaddateDesc(storePage);
        return page;
    }

    public Page GetPostPage(int pageNumber, int pageCount) {
        Pageable storePage = PageRequest.of(pageNumber, pageCount);
        Page page = postRepositore.findAllByOrderByPublishdateDesc(storePage);
        return page;
    }



    public Albumсover GetAlbumCoverById(Long alcoid) {
        return albumCoverRepository.getById(alcoid);
    }

    public void PublicTrackToChat(Track track, Clientdetail cd) {
        ChatMessage cm = new ChatMessage();
        cm.setFromname(cd.getCustname()+" "+cd.getCustsurname());
        cm.setFromuuid(cd.getUuid());
        cm.setBody(
                "<span>Ми опублікували новий трек! </span>"
                +"<audio id=\""+track.getUuid() + "\" controls>"
                +"<source src=\"/store/audio/"
                +cd.getUuid()+"/"
                +track.getFilename()+"\"/>"
                +"</audio>"
        );
        cm.setRoomuuid(chatmainroom);
        cm.setRtype(EChatRecordType.RECORD_TYPE_MEDIA.ordinal());
        messangerService.SaveMessage(cm);
        try {
            this.template.convertAndSend("/topic/"+chatmainroom, cm);
        } catch (Exception e) {
            logger.info("PublicTrackToChat Exception");
            e.printStackTrace();
        }

//        cm = new ChatMessage();
//        cm.setFromname(cd.getCustname()+" "+cd.getCustsurname());
//        cm.setTouuid(cd.getUuid());
//        cm.setBody("Ми опублікували новий трек! "
//                +"<audio id=\""+track.getUuid() + "\" controls>"
//                +"<source src=\"/store/audio/"
//                +cd.getUuid()+"/"
//                +track.getFilename()+"\">"
//                +"</audio>"
//        );
//
//        cm.setRoomuuid(messangerService.GetChatRoomByUUID(chattrackroom));
//        messangerService.SaveMessage(cm);
    }
}
