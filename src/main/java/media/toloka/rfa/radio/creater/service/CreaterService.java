package media.toloka.rfa.radio.creater.service;


import media.toloka.rfa.radio.creater.repository.AlbumRepository;
import media.toloka.rfa.radio.creater.repository.TrackRepository;
import media.toloka.rfa.radio.document.ClientDocumentEditController;
import media.toloka.rfa.radio.model.*;
import media.toloka.rfa.radio.model.enumerate.EDocumentStatus;
import media.toloka.rfa.radio.post.repositore.PostRepositore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public class CreaterService {

    final Logger logger = LoggerFactory.getLogger(ClientDocumentEditController.class);

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private PostRepositore postRepositore;

    public List<Album> GetAllAlbumsByCreater(Clientdetail cd) {
        List<Album> albumList = albumRepository.findByClientdetail(cd);
        return albumList;
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

    public void SaveTrackUploadInfo(Path destination, Clientdetail cd) {
        Track track = new Track();
        track.setStatus(EDocumentStatus.STATUS_LOADED);
        track.setFilename(destination.getFileName().toString());
        track.setPatch(destination.toString());
        track.setClientdetail(cd);
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
}
