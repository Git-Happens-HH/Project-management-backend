package githappens.hh.project_management_app.ControllerTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.EnumProjectRole;
import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.domain.ProjectRepository;
import githappens.hh.project_management_app.domain.UserProject;
import githappens.hh.project_management_app.domain.UserProjectRepository;
import githappens.hh.project_management_app.security.JwtUtil;
import githappens.hh.project_management_app.web.AppUserDetailsServiceImpl;
import githappens.hh.project_management_app.web.CurrentUserService;
import githappens.hh.project_management_app.web.ProjectRestController;
import githappens.hh.project_management_app.web.ProjectWebSocketController;

@WebMvcTest(ProjectRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProjectControllerTests {

        @Autowired
        private MockMvc mockmvc;

        @MockitoBean
        private ProjectRepository projectRepository;

        private Project project;

        private AppUser appUser;

        @MockitoBean
        private ProjectWebSocketController projectWebSocketController;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private JwtUtil jwtUtil;

        @MockitoBean
        private AppUserDetailsServiceImpl appUserDetailsService;

        @MockitoBean
        private UserProjectRepository userProjectRepository;

        @MockitoBean
        private CurrentUserService currentUserService;

        // Test data
        LocalDateTime time;
        List<AppUser> users;

        @BeforeEach
        void setUp() {

                appUser = new AppUser(); // <<< ADDED
                appUser.setAppUserId(1L); // <<< ADDED
                appUser.setUsername("tester"); // <<< ADDED

                project = new Project(
                                "Project controller test",
                                "Testing",
                                LocalDateTime.now());
                project.setProjectId(1L);
        }

        // GET projects

        @Test
        public void shouldReturnProjects() throws Exception {

                Project project1 = new Project("Test 1",
                                "Testing 1",
                                LocalDateTime.now());
                Project project2 = new Project("Test 2",
                                "Testing 2",
                                LocalDateTime.now());

                UserProject membership1 = new UserProject();
                membership1.setProject(project1);
                membership1.setRole(EnumProjectRole.owner);

                UserProject membership2 = new UserProject();
                membership2.setProject(project2);
                membership2.setRole(EnumProjectRole.member);

                when(currentUserService.getRequesterUserId())
                                .thenReturn(1L);

                when(userProjectRepository.findByAppUser_AppUserId(1L))
                                .thenReturn(List.of(membership1, membership2));

                mockmvc.perform(get("/api/projects"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].title").value("Test 1"))
                                .andExpect(jsonPath("$[0].role").value("owner"))
                                .andExpect(jsonPath("$[1].title").value("Test 2"))
                                .andExpect(jsonPath("$[1].role").value("member"));
        }

        // GET project by id

        @Test
        public void shouldReturnProjectById() throws Exception {
                when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

                mockmvc.perform(get("/api/projects/{projectId}", 1L))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Project controller test"));
        }

        // CREATE project

        @Test
        public void shouldCreateProject() throws Exception {

                when(currentUserService.getRequester()).thenReturn(appUser);

                when(projectRepository.save(any(Project.class)))
                                .thenReturn(project);

                when(userProjectRepository.save(any(UserProject.class)))
                                .thenReturn(new UserProject());

                mockmvc.perform(post("/api/projects")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(project)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.projectId").value(1L))
                                .andExpect(jsonPath("$.title").value("Project controller test"))
                                .andExpect(jsonPath("$.description").value("Testing"));

        }

        // // DELETE project

        @Test
        public void shouldDeleteProjectById() throws Exception {

                when(currentUserService.getRequester()).thenReturn(appUser);

                UserProject membership = new UserProject();
                membership.setRole(EnumProjectRole.owner);

                when(userProjectRepository.findUserProjectByUserIdAndProjectId(1L, 1L))
                                .thenReturn(membership);

                doNothing().when(projectRepository).deleteById(1L);

                mockmvc.perform(delete("/api/projects/{projectId}", 1L))
                                .andExpect(status().isOk());

        }

}
