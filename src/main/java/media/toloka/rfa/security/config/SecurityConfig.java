package media.toloka.rfa.security.config;

//https://stackoverflow.com/questions/74753700/cannot-resolve-method-antmatchers-in-authorizationmanagerrequestmatcherregis

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService uds;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .requestMatchers(
                        "/home",
                        "/register",
                        "/saveUser",
                        "/emit",
                        "/process/**",
                        "/seveform/**",
                        "/post/**",
                        "/**"
                ).permitAll()
                .requestMatchers(
                        "/css/**",
                        "/icons/**",
                        "/js/**",
                        "/pictures/**"
                ).permitAll()
                .requestMatchers(
                        "/assets/**",
                        "/savequestion"
                ).permitAll()
                .requestMatchers("/login/**",
                        "/logout",
                        "/registerRadioUser",
                        "/restorePsw",
                        "/sendmail",
                        "/setUserPassword").permitAll()
                .requestMatchers("/admin/**").hasAuthority("Admin")
                .requestMatchers("/user/**").hasAuthority("User")
                .requestMatchers("/creater/**").hasAuthority("Creator")
                .requestMatchers("/editor/**").hasAuthority("Editor,Admin")
                .requestMatchers("/moderator/**").hasAuthority("Moderator,Admin")
                .requestMatchers("/upload/**").hasAuthority("User,Creator,Admin,Editor,Moderator")
//                .requestMatchers("/upload/music/**").hasAnyAuthority("Editor,User,Admin,Creator")
                .anyRequest().authenticated();
                //.anyRequest().permitAll();
// tmp comment
        http.formLogin(fL -> fL
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/login/route")
                .permitAll()
        );

        http.logout(lOut -> {
            lOut.invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .permitAll();
        });
        return http.build();

    }
//
////    @Bean
////    public AuthenticationProvider authenticationProvider() {
////        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
////        authenticationProvider.setUserDetailsService(uds);
////        authenticationProvider.setPasswordEncoder(encoder);
////        return authenticationProvider;
////    }
}