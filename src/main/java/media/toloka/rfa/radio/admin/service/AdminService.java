package media.toloka.rfa.radio.admin.service;


import media.toloka.rfa.radio.admin.AdminController;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.contract.service.ContractService;
import media.toloka.rfa.radio.creater.service.CreaterService;
import media.toloka.rfa.radio.document.service.DocumentService;
import media.toloka.rfa.radio.history.service.HistoryService;
import media.toloka.rfa.radio.message.service.MessageService;
import media.toloka.rfa.radio.model.Clientaddress;
import media.toloka.rfa.radio.model.Documents;
import media.toloka.rfa.radio.model.Post;
import media.toloka.rfa.radio.post.service.PostService;
import media.toloka.rfa.radio.station.service.StationService;
import media.toloka.rfa.radio.store.Service.StoreService;
import media.toloka.rfa.security.model.Users;
import media.toloka.rfa.security.service.ServiceSecurityUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdminService {


    @Autowired
    private ClientService clientService;

    @Autowired
    private CreaterService createrService;

    @Autowired
    private PostService postService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private StationService stationService;

    @Autowired
    private StoreService storeService;


    final Logger logger = LoggerFactory.getLogger(AdminService.class);


    public List<Post> GetNotApruvePosts() {
        return postService.GetListPostByApruve(false);
    }

    public Post GetPostById(Long postId) {
        return postService.GetPostById(postId);
    }

    public void SavePost(Post post) {
        postService.SavePost(post);
    }

    public List<Users> GetAllUsers() {
       List<Users> usersList = clientService.GetAllUsers();
        return usersList;
    }

    public List<Documents> GetNotApruvedDocuments() {
        return documentService.GetUnApruvedDocumentsOrderLoaddate();
    }

    public List<Clientaddress> GetNotApruvedAddresses() {
        return clientService.GetUnApruvedDocumentsOrderLoaddate();
    }

    public Clientaddress GetClientAddress(Long idAddress) {
        return clientService.GetClientAddressById(idAddress);
    }

    public void SaveClientAddress(Clientaddress clientaddress) {
        clientService.SaveAddress(clientaddress);
    }
}
