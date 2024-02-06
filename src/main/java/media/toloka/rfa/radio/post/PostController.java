package media.toloka.rfa.radio.post;

import media.toloka.rfa.radio.document.ClientDocumentEditController;
import media.toloka.rfa.radio.post.repositore.PostRepositore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class PostController {
    final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostRepositore postRepositore;
}
