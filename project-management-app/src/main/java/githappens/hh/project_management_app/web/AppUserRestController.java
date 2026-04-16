package githappens.hh.project_management_app.web;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.AppUserRepository;
import githappens.hh.project_management_app.domain.Project;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AppUserRestController {
    private final AppUserRepository appUserRepository;

    public AppUserRestController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    // get all users
    @GetMapping("/api/users")
    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    // get user by id
    @GetMapping("/api/users/{userId}")
    public Optional<AppUser> getUserById(@PathVariable Long userId) {
        return appUserRepository.findById(userId);
    }

    // get projects for user
    @GetMapping("/api/users/{userId}/projects")
    public Iterable<Project> getProjectsForUser(@PathVariable Long userId) {
        return appUserRepository.findById(userId)
                .map(AppUser::getProjects)
                .orElse(List.of());
    }

    // create user
    @PostMapping("/api/users")
    public AppUser createUser(@RequestBody AppUser user) {
        if (appUserRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already in use");
        }
        if (appUserRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        return appUserRepository.save(user);
    }

    @DeleteMapping("/api/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        appUserRepository.deleteById(userId);
    }
}