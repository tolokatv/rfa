package media.toloka.rfa.radio.client.service;

import media.toloka.rfa.model.Clientaddress;
import media.toloka.rfa.model.Clientdetail;
import media.toloka.rfa.radio.client.ClientHomeInfoController;
import media.toloka.rfa.repository.ClientAddressRepository;
import media.toloka.rfa.repository.ClientDetailRepository;
//import media.toloka.rfa.repository.ClientRepository;
import media.toloka.rfa.repository.DocumentRepository;
import media.toloka.rfa.security.model.ERole;
import media.toloka.rfa.security.model.Roles;
import media.toloka.rfa.security.model.Users;
import media.toloka.rfa.security.repository.UserSecurityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class ClientService {


    @Autowired
    private ClientDetailRepository clientDetailRepository;
    @Autowired
    private ClientAddressRepository clientAddressRepository;
    @Autowired
    private UserSecurityRepository userSecurityRepository;

    @Autowired
    private DocumentRepository documentRepository;

    final Logger logger = LoggerFactory.getLogger(ClientHomeInfoController.class);


    public Users GetUserByEmail(String email) {
//        List<Users> usersList =
        return userSecurityRepository.getByEmail(email);
    }

    public Users GetCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        List<Users> usersList= userRepository.findUserByEmail(email);
        return GetUserByEmail(email);
    }

    public List<Roles> getListRole() {
        return GetCurrentUser().getRoles();
    }

    public boolean checkRole(ERole role) {
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

    public Users SaveUser(Users user) {
        return userSecurityRepository.save(user);
    }

//    public Optional<Users> findById(Long idUser) {
//        return userRepository.findById(idUser);
//    }

//    public Users getdById(Long idUser) {
//        return userRepository.getById(idUser);
//    }

    public Clientdetail GetClientDetailById(Long id) {
        return clientDetailRepository.getById(id);
    }

    public Clientdetail GetClientDetailByUser(Users user) {
        if (user == null) {
            return null;
        }
        List<Clientdetail> cdl = clientDetailRepository.getByUser(user.getId());
        if (cdl.isEmpty()) {
        return null;
        }
        if (cdl.size() > 1 ) {
            logger.info("Йой! Знайшли більше одної ClientDetail!!!");
            return null;
        }
        Clientdetail cd = cdl.get(0);
        return cd;
    }

    public void CreateClientsDetail(Users user, String name, String surname) {
        Clientdetail clientdetail = new Clientdetail();
        clientdetail.setUser(user.getId());
        clientdetail.setCustname(name);
        clientdetail.setCustsurname(surname);
        clientdetail.setUuid(UUID.randomUUID().toString());
        clientDetailRepository.save(clientdetail);

    }

    public void SaveClientDetail(Clientdetail curuserdetail) {
        clientDetailRepository.save(curuserdetail);
    }

    public Clientaddress GetAddress(Long id) {
        return clientAddressRepository.getById(id);
    }

    public void SaveAddress(Clientaddress fclientaddress) {
        clientAddressRepository.save(fclientaddress);
    }

    public List<Clientaddress> GetAddressList(Clientdetail clientdetail) { return clientAddressRepository.findByClientdetail(clientdetail); }

    public Users GetUserById(Long iduser) {
        return userSecurityRepository.getById(iduser);
    }
}

