package media.toloka.rfa.radio.client.service;

import media.toloka.rfa.radio.client.repository.ClientRepository;
import media.toloka.rfa.security.security.model.ERole;
import media.toloka.rfa.security.security.model.Roles;
import media.toloka.rfa.security.security.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Optional<Users> findUserByEmail(String email) {
        return clientRepository.findUserByEmail(email);
    }
    public Users GetCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> opt = clientRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        // Якщо не залогінені, то переходимо на головну.
        if (opt.isEmpty()) {
            return null;
        } else {
            return opt.get();
        }
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
        clientRepository.save(user);
//        return user.getId();
    }

    public Optional<Users> findById(Long idUser) {
        return clientRepository.findById(idUser);
    }
}
