package media.toloka.rfa.radio.post.service;

import media.toloka.rfa.radio.document.ClientDocumentEditController;
import media.toloka.rfa.radio.post.repositore.PostRepositore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private PostRepositore postRepositore;


}
