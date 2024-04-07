package media.toloka.rfa.radio.creater.repository;


import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long>, PagingAndSortingRepository<Track, Long> {

//    Optional
    List<Track> findByClientdetail(Clientdetail cd);
    Track getById(Long id);
    List<Track> findAllByOrderByUploaddateAsc();
    List<Track> findAllTop10ByOrderByUploaddateAsc();


    Page findAllByOrderByUploaddateDesc(Pageable storePage);

    Track getByStoreuuid(String storeUuid);
//    Page findAllOrderByUploaddateByAsc(Pageable storePage);
//
//    Page findAllOrderByUploaddateDesc(Pageable storePage);
//    findAllOrderByDateAsc
}
