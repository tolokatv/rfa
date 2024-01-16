package media.toloka.rfa.radio.client.service;

import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.client.repository.ClientAddressRepository;
import media.toloka.rfa.radio.client.repository.ClientDetailRepository;
import media.toloka.rfa.radio.client.repository.UserRepository;
import media.toloka.rfa.radio.document.model.Documents;
import media.toloka.rfa.radio.document.repo.DocumentRepository;
import media.toloka.rfa.security.model.ERole;
import media.toloka.rfa.security.model.Roles;
import media.toloka.rfa.security.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientDetailRepository clientDetailRepository;
    @Autowired
    private ClientAddressRepository clientAddressRepository;

    @Autowired
    private DocumentRepository documentRepository;

//    public Optional<Users> getByEmail(String email) {
    public Users getByEmail(String email) {
//        return clientRepository.getClientByEmail(email);
        return userRepository.getUserByEmail(email);
    }
    public Users GetCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        Optional<Users> opt = clientRepository.getByUser(email); //ByEmail("ysv@toloka.kiev.ua") ;
//        // Якщо не залогінені, то переходимо на головну.
        return userRepository.getUserByEmail(email); //ByEmail("ysv@toloka.kiev.ua");
//        if (opt.isEmpty()) {
//            return null;
//        } else {
//            return opt.get();
//        }
//        return clientRepository.getByUser(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public List<Roles> getListRole () {
        return GetCurrentUser().getRoles();
    }

    public boolean checkRole (ERole role) {
        List<Roles> roles = getListRole();
        Iterator<Roles> iterator = roles.iterator();
        while (iterator.hasNext()) {
            Roles curRole = iterator.next();
            if (role.equals(curRole.getRole())) {
                return true;
            }
        }
        return false;
    }

    public void saveUser(Users user) {
        userRepository.save(user);
//        return user.getId();
    }

    public Optional<Users> findById(Long idUser) {
        return userRepository.findById(idUser);
    }

    public Documents GetDocument(Long idDocument) {
        // витягли з бази документ
        return documentRepository.getById(idDocument);
    }

    public void saveDocument(Documents document) {
        // зберегли документ
        documentRepository.save(document);
    }

    public Clientdetail getClientDetail(Users frmuser) {
        // витягли з бази деталі по клієнту
//        Optional<Clientdetail> ocd = clientDetailRepository.getByUser(frmuser);
        return clientDetailRepository.getByUser(frmuser);
//        if (ocd.isEmpty()) {
//            return null;
//        }
//        else {
//            return ocd.get();
//        }
    }

    public void CreateClientsDetail(Users user,String name,String surname) {
        Clientdetail clientdetail = new Clientdetail();
        clientdetail.setUser(user);
        clientdetail.setCustname(name);
        clientdetail.setCustsurname(surname);
        clientdetail.setUuid(UUID.randomUUID().toString());
        clientDetailRepository.save(clientdetail);

    }

}
