package githappens.hh.project_management_app.ControllerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.LinkedHashMap;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.AppUserRepository;
import githappens.hh.project_management_app.security.JwtUtil;
import githappens.hh.project_management_app.web.AppUserDetailsServiceImpl;
import githappens.hh.project_management_app.web.AppUserRestController;
import githappens.hh.project_management_app.web.AuthController;

import org.springframework.http.MediaType;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTests {

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private AppUserDetailsServiceImpl appUserDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private AppUserRestController appUserRestController;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private AppUserRepository appUserRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private AppUser user;

    @BeforeEach
    void setUp() {
        user = new AppUser();

        user.setEmail("test@test.com");
        user.setPasswordHash("testisalasana");
        user.setFirstName("Timo");
        user.setLastName("Testi");
        user.setUsername("TestiTintti");
    }

    // LOGIN SUCCESSFUL

    @Test
    public void shouldLoginSuccessfully() throws Exception {

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal())
                .thenReturn(userDetails);
        when(userDetails.getUsername())
                .thenReturn("test@test.com");

        when(jwtUtil.generateToken("test@test.com"))
                .thenReturn("fake-jwt-token");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("fake-jwt-token"));
    }

    // LOGIN FAILURE

    @Test
    public void shouldRejectInvalidLogin() throws Exception {

        AppUser invalidUser = new AppUser();
        invalidUser.setEmail("invalid@example.com");
        invalidUser.setPasswordHash("wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException("Bad credentials"));

         mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidUser)))
            .andExpect(status().isUnauthorized())
            .andExpect(content().string("Invalid email or password"));
    }

    
    // REGISTER SUCCESSFUL
    @Test
    public void shouldRegisterSuccessfully() throws Exception {
        when(appUserRepository.existsByUsername("TestiTintti")).thenReturn(false);
        when(appUserRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(appUserRepository.save(any(AppUser.class))).thenReturn(user);
        
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User registered succesfully!"));

        verify(appUserRepository).save(any(AppUser.class));
    }

    // REJECT DUPLICATE EMAIL

    @Test
    public void shouldRejectDuplicateEmail() throws Exception {
        AppUser duplicateEmailUser = new AppUser();
        duplicateEmailUser.setEmail("test@test.com");
        duplicateEmailUser.setPasswordHash("testisalasana");
        duplicateEmailUser.setFirstName("John");
        duplicateEmailUser.setLastName("Duplicate-User");
        duplicateEmailUser.setUsername("JohnDuplicate");

        when(appUserRepository.save(any(AppUser.class))).thenReturn(duplicateEmailUser);
        when(appUserRepository.existsByEmail("test@test.com")).thenReturn(true);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Error: An account is already registered with this email!"));
        

//                 @ExceptionHandler(BadCredentialsException.class)
//     @ResponseStatus(HttpStatus.UNAUTHORIZED)
//     public String handleBadCredentials(BadCredentialsException ex) {
//         return "Invalid email or password";
//     }
// }


    }

    // SHOULD ENCODE PASSWORD BEFORE SAVING

    @Test
    public void shouldEncodePassword() throws Exception {
        
        when(appUserRepository.existsByEmail("test@test.com"))
        .thenReturn(false);
        
        when(passwordEncoder.encode("testisalasana"))
        .thenReturn("kryptattusalasana");

    Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("username", user.getUsername());
        requestBody.put("firstName", user.getFirstName());
        requestBody.put("lastName", user.getLastName());
        requestBody.put("email", user.getEmail());
        requestBody.put("passwordHash", user.getPasswordHash());

         mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isOk())
            .andExpect(content().string("User registered succesfully!"));

        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(captor.capture());
        AppUser savedUser = captor.getValue();

        assertEquals("kryptattusalasana", savedUser.getPasswordHash());

}

}