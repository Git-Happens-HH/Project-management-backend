package githappens.hh.project_management_app.web;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.AppUserRepository;
import githappens.hh.project_management_app.security.JwtUtil;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AppUserRepository AppUserRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtil jwtUtils;
    @PostMapping("/login")
    public String authenticateUser(@RequestBody AppUser appUser) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                appUser.getEmail(), 
                appUser.getPasswordHash()
            )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtils.generateToken(userDetails.getUsername());
    }


@PostMapping("/register")
public String registerAppUser(@RequestBody AppUser appUser) {
    if (AppUserRepository.existsByEmail(appUser.getEmail())) {
        return "Error: Username is already taken!";
    }
    AppUser newUser = new AppUser(
        appUser.getUsername(),
        appUser.getFirstName(),
        appUser.getLastName(),
        appUser.getEmail(),
        encoder.encode(appUser.getPasswordHash()),
        appUser.getRegisteredAt()
    );
    AppUserRepository.save(newUser);
    return "User registered succesfully!";
    }
}