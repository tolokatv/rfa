package media.toloka.rfa.radio.creater;

import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.creater.service.CreaterService;
import media.toloka.rfa.radio.document.ClientDocumentEditController;
import media.toloka.rfa.radio.model.Album;
import media.toloka.rfa.radio.model.Clientaddress;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Post;
import media.toloka.rfa.radio.post.service.PostService;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CreaterController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CreaterService createrService;

    @Autowired
    private PostService postService;

    final Logger logger = LoggerFactory.getLogger(ClientDocumentEditController.class);


    @GetMapping(value = "/creater/home")
    public String getUserHomeInfo(
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }

        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        List<Post> posts = createrService.GetAllPostsByCreater(cd);
        model.addAttribute("posts", posts );

        return "/creater/home";
    }

    @GetMapping(value = "/creater/editpost/{idPost}")
    public String getCreaterEditPost(
            @PathVariable Long idPost,
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }

        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        if (idPost == 0L ) {
            logger.info("Створюємо новий пост");
        }
//        List<Post> posts = createrService.GetAllPostsByCreater(cd);
//        model.addAttribute("posts", posts );
        Post post;

        if (idPost == 0L ) {
            post = new Post();
            post.setId(0L);
        } else {
            post = postService.GetPostById(idPost);
        }
        model.addAttribute("post", post );

        return "/creater/editpost";
    }

    @PostMapping(value="/creater/editpost/{idPost}")
    public String postCreaterEditPost(
    @PathVariable Long idPost,
    @ModelAttribute Post fPost,
    Model model )

    {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }

        Clientdetail cd = clientService.GetClientDetailByUser(clientService.GetCurrentUser());
        Post post;
        if (idPost == 0L) {
            logger.info("Створюємо новий пост");
            post = new Post();

        } else {
            post = postService.GetPostById(idPost);
        }
        post.setPostbody(fPost.getPostbody());
        post.setPosttitle(fPost.getPosttitle());
        post.setClientdetail(cd);
        postService.SavePost(post);

        List<Post> posts = createrService.GetAllPostsByCreater(cd);
        model.addAttribute("posts", posts );
        return "/creater/home";
    }

}
