package media.toloka.rfa.radio.post.repositore;

import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepositore extends JpaRepository<Post, Long> {
    List<Post> findByClientdetail(Clientdetail cd);
    Post getById(Long id);
}
