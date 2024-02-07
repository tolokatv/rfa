package media.toloka.rfa.radio.post;

import media.toloka.rfa.radio.document.ClientDocumentEditController;
import media.toloka.rfa.radio.model.Post;
import media.toloka.rfa.radio.post.repositore.PostRepositore;
import media.toloka.rfa.radio.post.service.PostService;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PostController {
    final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostRepositore postRepositore;
    @Autowired
    private PostService postService;

    @GetMapping(value = "/post/postview/{idPost}") // /post/postview/52
    public String getViewPost(
            @PathVariable Long idPost,
            Model model) {
        Post post = postService.GetPostById(idPost);
        if (post == null){
            return "redirect:/";
        }
        model.addAttribute("post", post );

        return "/post/postview";
    }
}
