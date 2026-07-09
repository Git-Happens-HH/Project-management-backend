package githappens.hh.project_management_app.ControllerTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.AppUserRepository;
import githappens.hh.project_management_app.security.JwtUtil;
import githappens.hh.project_management_app.web.AppUserDetailsServiceImpl;
import githappens.hh.project_management_app.web.AppUserRestController;
import githappens.hh.project_management_app.web.ProjectRestController;


@WebMvcTest(AppUserRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AppUserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppUserRepository appUserRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;
    
    private AppUser user;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private AppUserDetailsServiceImpl appUserDetailsService;

    @BeforeEach
    void setUp() {
        user = new AppUser();
        user.setAppUserId(1L);
        user.setUsername("P-python");
        user.setFirstName("Paula");
        user.setLastName("Python");
        user.setEmail("paulapython@example.com");
        user.setPasswordHash(passwordEncoder.encode("Salasana@123"));
        user.setRegisteredAt(LocalDateTime.now());

    }

    // Return all users test
    @Test
    void shouldReturnAllUsers() throws Exception {

        when(appUserRepository.findAll())
                .thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("P-python"))
                .andExpect(jsonPath("$[0].email").value("paulapython@example.com"));
}

    // Return user by id test
    @Test
    void shouldReturnUserById() throws Exception {

        when(appUserRepository.findById(1L))
                .thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("P-python"));
    }


    // CREATE user test
    @Test
    void shouldCreateUsers() throws Exception {

        when(appUserRepository.existsByUsername("P-python")).thenReturn(false);
        when(appUserRepository.existsByEmail("paulapython@example.com")).thenReturn(false);
        when(appUserRepository.save(any(AppUser.class))).thenReturn(user);


        // Test POST /api/users
        mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("P-python"));

    }

    // Reject duplicate username test
    @Test
    void shouldRejectDuplicateUsername() throws Exception {

        when(appUserRepository.existsByUsername("P-python"))
                .thenReturn(true);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    // DELETE user test
    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
        .andExpect(status().isOk());

        verify(appUserRepository).deleteById(1L);
    }
    
}
