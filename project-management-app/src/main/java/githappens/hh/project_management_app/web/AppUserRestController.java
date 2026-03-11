package githappens.hh.project_management_app.web;

import java.util.List;
import java.util.Optional;
import java.util.Collections;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.AppUserRepository;
import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.domain.ProjectRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AppUserRestController {
    private final AppUserRepository appUserRepository;
    private final ProjectRepository projectRepository;

    public AppUserRestController(AppUserRepository appUserRepository, ProjectRepository projectRepository) {
        this.appUserRepository = appUserRepository;
        this.projectRepository = projectRepository;
    }

    @GetMapping("/api/users")
    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    @GetMapping("/api/users/{userId}")
    public Optional<AppUser> getUserById(@PathVariable Long userId) {
        return appUserRepository.findById(userId);
    }

    @GetMapping("/api/users/{userId}/projects")
    public Iterable<Project> getProjectsForUser(@PathVariable Long userId) {
        return appUserRepository.findById(userId)
                .map(user -> projectRepository.findByAssignedUser(user))
                .orElse(Collections.emptyList());
    }

    @PostMapping("/api/users")
    public AppUser createUser(@RequestBody AppUser user) {
        return appUserRepository.save(user);
    }

    @DeleteMapping("/api/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        appUserRepository.deleteById(userId);
    }
}