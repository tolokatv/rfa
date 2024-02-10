package media.toloka.rfa.radio.creater.repository;


import media.toloka.rfa.radio.model.Album;
import media.toloka.rfa.radio.model.AlbumCover;
import media.toloka.rfa.radio.model.Clientdetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumCoverRepository extends JpaRepository<AlbumCover, Long> {

//    Optional
    List<AlbumCover> findByClientdetail(Clientdetail cd);
    AlbumCover getById(Long id);
}
