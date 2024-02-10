package media.toloka.rfa.radio.creater.repository;


import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {

//    Optional
    List<Track> findByClientdetail(Clientdetail cd);
    Track getById(Long id);
    List<Track> findAllByOrderByUploaddateAsc();
    List<Track> findAllTop10ByOrderByUploaddateAsc();
//    findAllOrderByDateAsc
}
