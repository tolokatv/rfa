package media.toloka.rfa.security.security.service;

import media.toloka.rfa.security.security.model.Roles;
import media.toloka.rfa.security.security.model.Users;
import media.toloka.rfa.security.security.repository.UserSecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ServiceSecurityUserDetails implements ServiceSecurityUsers, UserDetailsService {
//    public class UserDetailsServiceImpl implements IUserService, UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserSecurityRepository repoUsers;
    @Override
    public Long saveUser(Users user) { // TODO Перенести з класу повʼязаного з Security до класу обробки реєстрації користувача
        String passwd= user.getPassword();
        String encodedPasswod = passwordEncoder.encode(passwd);
        user.setPassword(encodedPasswod);
        user = repoUsers.save(user);
        return user.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        org.springframework.security.core.userdetails.User springUser=null;

        Optional<Users> opt = repoUsers.findUserByEmail(email);
        if(opt.isEmpty()) {
//            Users auser = new Users();
//            auser.setEmail("ysv@toloka.kiev.ua");
//            auser.setPassword("1");
//            auser.setRoles(new ArrayList<Roles>());
//            Roles roles = new Roles();
//            roles.setRole(ERole.ROLE_USER);
//            auser.getRoles().add(roles);
//            saveUser(auser);
            throw new UsernameNotFoundException("Користувача з поштою " +email +" не знайдено.");
        }else {
            Users user = opt.get();
            List<Roles> roles = user.getRoles();
            Set<GrantedAuthority> ga = new HashSet<>();
            // TODO Тут щось підправив
            for(Roles role:roles) {
                ga.add(new SimpleGrantedAuthority(role.getRole().label));
            }
            springUser = new org.springframework.security.core.userdetails.User(
                    email,
                    user.getPassword(),
                    ga );
        }
        return springUser;
    }


}
