package githappens.hh.project_management_app.ControllerTests;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.web.ProjectRestController;

@WebMvcTest(ProjectRestController.class)
public class ProjectControllerTests {

    @Autowired
    private MockMvc mockmvc;

    private Project project;

    private AppUser appUser;

    // Test data
    LocalDateTime time;
    List<AppUser> users;

    
    @BeforeEach
    void setUp() {
       project = new Project(
        "Project controller test", 
        "Testing",
         time.now(), 
         false);
    }


     // get projects
    // @GetMapping("/api/projects")
    // public @ResponseBody List<Project> listProjects() {
    //     return (List<Project>) projectRepository.findAll();
    // }


    @Test
    public void shouldReturnProjects() throws Exception {


    }



    // // get project by id
    // @GetMapping("/api/projects/{projectId}")
    // public @ResponseBody Optional<Project> getProjectById(@PathVariable Long projectId) {
    //     return projectRepository.findById(projectId);
    // }

    // // CREATE project
    // @PostMapping("/api/projects")
    // public @ResponseBody Project createProject(@RequestBody Project project) {
    //     return projectRepository.save(project);
    // }

    // // DELETE project
    // @DeleteMapping("/api/projects/{projectId}")
    // public void deleteProject(@PathVariable Long projectId) {
    //     projectRepository.deleteById(projectId);
    // }

    
}
