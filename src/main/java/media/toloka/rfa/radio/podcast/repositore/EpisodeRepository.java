package media.toloka.rfa.radio.podcast.repositore;

import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.podcast.model.PodcastChannel;
import media.toloka.rfa.radio.podcast.model.PodcastItem;
import media.toloka.rfa.radio.store.model.EStoreFileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface EpisodeRepository
        extends JpaRepository<PodcastItem, Long>, PagingAndSortingRepository<PodcastItem, Long> {
//    List<PodcastItem> findByClientdetail(Clientdetail cd);
//    PodcastChannel getByStoreuuid(String storeUuid);
    PodcastItem getByUuid(String ItemUuid);
    PodcastItem save(PodcastItem episode);

    List<PodcastItem> findByClientdetailOrderByIdDesc(Clientdetail cd);

}
