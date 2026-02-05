package githappens.hh.project_management_app.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

@GetMapping("api/projects/projectId")
public @ResponseBody Project getProjectById(@PathVariable Long projectId) {
    return projectRepository.findById(projectId)

}
}