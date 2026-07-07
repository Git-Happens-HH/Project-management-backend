package githappens.hh.project_management_app.ControllerTests;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import githappens.hh.project_management_app.domain.Project;

@WebMvcTest
public class ProjectControllerTests {

    // test data
    private final Long projectId = 1L;

    @Autowired
    private Project project;

    @Autowired
    private MockMvc mockmvc;



     // get projects
    // @GetMapping("/api/projects")
    // public @ResponseBody List<Project> listProjects() {
    //     return (List<Project>) projectRepository.findAll();
    // }

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

    @Test
    public void shouldReturnProjects() throws Exception {


    }

    
}
