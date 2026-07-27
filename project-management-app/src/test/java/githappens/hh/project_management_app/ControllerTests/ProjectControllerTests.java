package githappens.hh.project_management_app.ControllerTests;

import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.domain.ProjectRepository;
import githappens.hh.project_management_app.security.JwtUtil;
import githappens.hh.project_management_app.web.AppUserDetailsServiceImpl;
import githappens.hh.project_management_app.web.ProjectRestController;

@WebMvcTest(ProjectRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProjectControllerTests {

    @Autowired
    private MockMvc mockmvc;

    @MockitoBean
    private ProjectRepository projectRepository;

    private Project project;

    private AppUser appUser;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private AppUserDetailsServiceImpl appUserDetailsService;

    // Test data
    LocalDateTime time;
    List<AppUser> users;

    @BeforeEach
    void setUp() {
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

        List<Project> projects = List.of(project1, project2);

        when(projectRepository.findAll()).thenReturn(projects);

        mockmvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Test 1"))
                .andExpect(jsonPath("$[1].title").value("Test 2"));
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

        when(projectRepository.save(any(Project.class)))
                .thenReturn(project);

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

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        mockmvc.perform(delete("/api/projects/{projectId}", 1L))
                .andExpect(status().isOk());

    }

}
