package media.toloka.rfa.podcast.repositore;

import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.podcast.model.PodcastImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface PodcastCoverRepository
        extends JpaRepository<PodcastImage, Long>, PagingAndSortingRepository<PodcastImage, Long> {
    List<PodcastImage> findByClientdetailOrderByIdDesc(Clientdetail cd);
    PodcastImage getByUuid(String CoverUuid);
    PodcastImage save(PodcastImage podcastImage);

}
