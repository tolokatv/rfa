package media.toloka.rfa.radio.client.service;

import media.toloka.rfa.radio.model.Clientaddress;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.client.ClientHomeInfoController;
import media.toloka.rfa.radio.repository.ClientAddressRepository;
import media.toloka.rfa.radio.repository.ClientDetailRepository;
import media.toloka.rfa.radio.repository.UserRepository;
import media.toloka.rfa.radio.repository.DocumentRepository;
import media.toloka.rfa.security.model.ERole;
import media.toloka.rfa.security.model.Roles;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
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

    final Logger logger = LoggerFactory.getLogger(ClientHomeInfoController.class);


    public Users GetUserByEmail(String email) {
//        List<Users> usersList =
        return userRepository.getUserByEmail(email);
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

    public boolean checkRole(Users user, ERole role) {
        List<Roles> roles = user.getRoles();
        Iterator<Roles> iterator = roles.iterator();
        while (iterator.hasNext()) {
            Roles curRole = iterator.next();
            if (role.equals(curRole.getRole())) {
                return true;
            }
        }
        return false;
    }

    public void SaveUser(Users user) {

        userRepository.save(user);
    }

    public Clientdetail GetClientDetailById(Long id) {

        return clientDetailRepository.getById(id);
    }

    public Clientdetail GetClientDetailByUUID(String uuid) {
        return clientDetailRepository.getByUuid(uuid);
    }

    public Clientdetail GetClientDetailByUser(Users user) {
        // todo якась херня :(
        if (user == null) {
            return null;
        }
        List<Clientdetail> cdl = clientDetailRepository.getByUser(user);
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
//        Clientdetail clientdetail = new Clientdetail();
        Clientdetail clientdetail = user.getClientdetail();
        clientdetail.setUser(user);
        clientdetail.setCustname(name);
        clientdetail.setCustsurname(surname);
        clientdetail.setUuid(UUID.randomUUID().toString());
//        clientDetailRepository.save(clientdetail);
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

    public List<Clientaddress> GetAddressList(Clientdetail Clientdetailrfa) {
        return clientAddressRepository.findByClientdetail(Clientdetailrfa);
    }

    public Users GetUserById(Long iduser) {
        return userRepository.getById(iduser);
    }

    public List<Clientaddress> GetClientAddressList(Clientdetail clientdetail) {
        List<Clientaddress> cal = clientAddressRepository.findByClientdetail(clientdetail);
        return cal;
    }

    public boolean ClientCanDownloadFile(Clientdetail cd) {
        // перевіряємо, чи може клієнт завантажувати файли
        // причини, чому може не мати права:
        // - перевищено ліміт для зберігання;
        // - помічено, що завантажує всяку дурню
        // ..... Ще придумаю.
        return true;
    }

    public Clientdetail GetClientDetailByUuid(String clientUUID) {
        Clientdetail cd = clientDetailRepository.getByUuid(clientUUID);
        return cd;

    }

    public List<Users> GetAllUsers() {
        return userRepository.findAll();
    }

    public List<Clientaddress> GetUnApruvedDocumentsOrderLoaddate() {
        return clientAddressRepository.findByApruve(false);
    }

    public Clientaddress GetClientAddressById(Long idAddress) {
        return clientAddressRepository.getById(idAddress);
    }

//    public void DeleteUser(Users curuser) {
//        userRepository.delete(curuser);
//    }

    public String GetCurrentTrack(URL url) {
        StringBuilder json = null;
        try { //(
            InputStream input = url.openStream();
        // ) {
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(isr);
            json = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                json.append((char) c);
            }

        } catch (IOException e) {
            logger.error("Помилка при отриманні інформації зі станції про поточний трек.");
            e.printStackTrace();
        }
        return json.toString();
    }
}

