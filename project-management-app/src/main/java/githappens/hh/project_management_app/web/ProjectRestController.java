package githappens.hh.project_management_app.web;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import githappens.hh.project_management_app.domain.EnumProjectRole;
import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.domain.ProjectRepository;
import githappens.hh.project_management_app.domain.UserProjectRepository;

@RestController
public class ProjectRestController {
    
    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    
    public ProjectRestController(ProjectRepository projectRepository, UserProjectRepository userProjectRepository) {
        this.projectRepository = projectRepository;
        this.userProjectRepository = userProjectRepository;
    }


    // get projects
    @GetMapping("/api/projects")
    public @ResponseBody List<Project> listProjects() {
        return (List<Project>) projectRepository.findAll();
    }

    // get project by id
    @GetMapping("/api/projects/{projectId}")
    public @ResponseBody Optional<Project> getProjectById(@PathVariable Long projectId) {
        return projectRepository.findById(projectId);
    }

    // CREATE project
    @PostMapping("/api/projects")
    public @ResponseBody Project createProject(@RequestBody Project project) {
        return projectRepository.save(project);
    }

    // DELETE project
    @DeleteMapping("/api/projects/{projectId}")
    public void deleteProject(@PathVariable Long projectId) {
        projectRepository.deleteById(projectId);
    }

// __________________________________________________________________________________________

    // get projects where user is owner or member
    @GetMapping("/api/projectsbyuser/{userId}")
    public @ResponseBody List<Project> getProjectsByUserIdAndRole(@PathVariable Long userId, @RequestParam EnumProjectRole role) {
        return userProjectRepository.findProjectsByUserIdAndRole(userId, role);
    }
    

}
