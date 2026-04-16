package githappens.hh.project_management_app.web;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.AppUserRepository;
import githappens.hh.project_management_app.security.JwtUtil;

import org.springframework.security.authentication.AuthenticationManager;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> registerAppUser(@RequestBody AppUser appUser) {
        if (AppUserRepository.existsByEmail(appUser.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is already taken!");
        }

        String rawPassword = appUser.getPasswordHash();
        if (rawPassword == null || rawPassword.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Password is required");
        }

        AppUser newUser = new AppUser(
            appUser.getUsername(),
            appUser.getFirstName(),
            appUser.getLastName(),
            appUser.getEmail(),
            encoder.encode(rawPassword),
            LocalDateTime.now()
        );
        AppUser saved = AppUserRepository.save(newUser);

        // Generate JWT for the newly created user. Use email as subject since login uses email.
        String token = jwtUtils.generateToken(saved.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", token));

// generate JWT for the newly created user (use email as subject if your auth uses email)
     // String subjectForToken = appUser.getEmail(); // or appUser.getUsername() depending how JwtUtil/userDetails are configured
     //   String token = jwtUtils.generateToken(subjectForToken);

     //return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
          //  "username", saved.getUserName() != null ? saved.getUserName() : saved.getUsername(),
            // "email", saved.getEmail(),
          // "token", token
       // ));

    }
}