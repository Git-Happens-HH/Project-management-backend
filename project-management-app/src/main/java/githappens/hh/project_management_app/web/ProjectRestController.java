package githappens.hh.project_management_app.web;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.domain.ProjectRepository;

@RestController
public class ProjectRestController {
    private final ProjectRepository projectRepository;

    public ProjectRestController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/api/projects")
    public @ResponseBody List<Project> listProjects() {
        return (List<Project>) projectRepository.findAll();
    }

    @GetMapping("/api/projects/{projectId}")
    public @ResponseBody Optional<Project> getProjectById(@PathVariable Long projectId) {
        return projectRepository.findById(projectId);
    }

    @PostMapping("/api/projects")
    public @ResponseBody Project createProject(@RequestBody Project project) {
        return projectRepository.save(project);
    }

    @DeleteMapping("/api/projects/{projectId}")
    public void deleteProject(@PathVariable Long projectId) {
        projectRepository.deleteById(projectId);
    }
}