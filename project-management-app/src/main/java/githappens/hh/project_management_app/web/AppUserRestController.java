package githappens.hh.project_management_app.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.AppUserRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AppUserRestController {
    private final AppUserRepository appUserRepository;

    public AppUserRestController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/api/users")
    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    @GetMapping("/api/users/{userId}")
    public AppUser getUserById(@PathVariable Long userId) {
        return appUserRepository.findById(userId);
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