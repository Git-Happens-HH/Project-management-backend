package githappens.hh.project_management_app.RepositoryTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.AppUserRepository;
import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@SpringBootTest
public class AppUserRepositoryTests {

    @Autowired
    private AppUserRepository appUserRepository;

    // CREATE new user test
    @Test
    public void createNewUser() {
        AppUser user = new AppUser("test1", "Test", "User", "test1@hh.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user);
        assertThat(user.getAppUserId()).isNotNull();
        assertThat(user.getUsername()).isEqualTo("test1");
        assertThat(user.getFirstName()).isEqualTo("Test");
        assertThat(user.getLastName()).isEqualTo("User");
        assertThat(user.getEmail()).isEqualTo("test1@hh.com");
        assertThat(user.getPasswordHash()).isEqualTo("Test123!");
        assertThat(user.getTasksAssigned()).hasSize(0);
        assertThat(user.getProjects()).hasSize(0);
    }

    // SEARCH user by username test
    @Test
    public void findByUsernameShouldReturnUser() {
        AppUser user = new AppUser("test2", "Test", "User", "test2@hh.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user);
        Optional<AppUser> found = appUserRepository.findByUsername("test2");
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("test2");
        assertThat(found.get().getEmail()).isEqualTo("test2@hh.com");
    }

    // SEARCH user by id test
    @Test
    public void findByIdShouldReturnUser() {
        AppUser user = new AppUser("test3", "Test", "User", "test3@hh.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user);
        Optional<AppUser> found = appUserRepository.findById(user.getAppUserId());
        assertThat(found).isPresent();
        assertThat(found.get().getAppUserId()).isEqualTo(user.getAppUserId());
        assertThat(found.get().getUsername()).isEqualTo("test3");
        assertThat(found.get().getEmail()).isEqualTo("test3@hh.com");
    }

    // DELETE user by id test
    @Test
    public void deleteUserShouldRemoveFromRepository() {
        AppUser user = new AppUser("test4", "Test", "User", "test4@hh.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user);
        Long id = user.getAppUserId();
        appUserRepository.deleteById(id);
        assertThat(appUserRepository.findById(id)).isEmpty();
    }

    // CUSTOM QUERY: FIND BY USERNAME ignore case
    @Test
    public void findByUsernameContainingIgnoreCaseShouldReturnUsers() {
        AppUser user1 = new AppUser("TestUser", "Test", "User", "testuser@hh.com", "Test123!", LocalDateTime.now());
        AppUser user2 = new AppUser("AnotherTest", "Another", "Test", "anothertest@hh.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user1);
        appUserRepository.save(user2);

        List<AppUser> found = (List<AppUser>) appUserRepository.findByUsernameContainingIgnoreCase("test");
        assertThat(found).hasSize(2);
        assertThat(found).extracting(AppUser::getUsername).contains("TestUser", "AnotherTest");
    }

    // CUSTOM QUERY: FIND BY EMAIL
    @Test
    public void findByEmailShouldReturnUser() {
        AppUser user = new AppUser("test5", "Test", "User", "test5@hh.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user);
        Optional<AppUser> found = appUserRepository.findByEmail("test5@hh.com");
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("test5");
    }

    // CUSTOM QUERY: EXISTS BY EMAIL
    @Test
    public void existsByEmailShouldReturnTrue() {
        AppUser user = new AppUser("test6", "Test", "User", "test6@hh.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user);
        boolean exists = appUserRepository.existsByEmail("test6@hh.com");
        assertThat(exists).isTrue();
    }

    // CUSTOM QUERY: EXISTS BY USERNAME
    @Test
    public void existsByUsernameShouldReturnTrue() {
        AppUser user = new AppUser("test7", "Test", "User", "test7@hh.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user);
        boolean exists = appUserRepository.existsByUsername("test7");
        assertThat(exists).isTrue();
    }
}