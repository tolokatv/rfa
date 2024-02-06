package media.toloka.rfa.radio.creater.service;


import media.toloka.rfa.radio.creater.repository.AlbumRepository;
import media.toloka.rfa.radio.creater.repository.TrackRepository;
import media.toloka.rfa.radio.document.ClientDocumentEditController;
import media.toloka.rfa.radio.model.Album;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Post;
import media.toloka.rfa.radio.model.Track;
import media.toloka.rfa.radio.post.repositore.PostRepositore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
