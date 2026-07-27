package githappens.hh.project_management_app.RepositoryTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.AppUserRepository;
import githappens.hh.project_management_app.domain.EnumProjectRole;
import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.domain.ProjectRepository;
import githappens.hh.project_management_app.domain.UserProject;
import githappens.hh.project_management_app.domain.UserProjectRepository;
import jakarta.transaction.Transactional;

@Transactional
@SpringBootTest
public class UserProjectRepositoryTests {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Test
    void savingUserProjectShouldPersistJoinedAt() {
        AppUser user = new AppUser("member-user", "Member", "User", "member@example.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user);

        Project project = new Project("Membership Test Project", "Testing joins", LocalDateTime.now());
        projectRepository.save(project);

        UserProject membership = new UserProject();
        membership.setAppUser(user);
        membership.setProject(project);
        membership.setRole(EnumProjectRole.member);
        membership.setJoinedAt(LocalDateTime.now());

        UserProject saved = userProjectRepository.save(membership);

        assertThat(saved.getJoinedAt()).isNotNull();
        assertThat(saved.getAppUser().getAppUserId()).isEqualTo(user.getAppUserId());
        assertThat(saved.getProject().getProjectId()).isEqualTo(project.getProjectId());
    }
}
