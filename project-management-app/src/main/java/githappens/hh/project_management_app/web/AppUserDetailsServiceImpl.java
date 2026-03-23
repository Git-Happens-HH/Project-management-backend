package githappens.hh.project_management_app.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.AppUserRepository;

@Service
public class AppUserDetailsServiceImpl implements UserDetailsService {
 
     private final AppUserRepository appUserRepository;

    public AppUserDetailsServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser currUser = appUserRepository.findByUsername(username).orElse(null);

    // empty list of authorities, since we don't have roles (yet)
         List<GrantedAuthority> authorities = new ArrayList<>();
       
        // Found user will be wrapped in a Spring Security 'User' object (username + password + role)
        return new org.springframework.security.core.userdetails.User(
                currUser.getUsername(),
                currUser.getPasswordHash(),
                authorities); // empty list so constructor passes
    }

}
