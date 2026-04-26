package githappens.hh.project_management_app.RepositoryTests;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.AppUserRepository;
import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.domain.ProjectRepository;
import githappens.hh.project_management_app.domain.Task;
import githappens.hh.project_management_app.domain.TaskList;
import githappens.hh.project_management_app.domain.TaskListRepository;
import githappens.hh.project_management_app.domain.TaskRepository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

// This test class' code has been generated using Claude Sonnet 4.6-language model

@ActiveProfiles("testcontainer")
@SpringBootTest
public class ExamplePostgres extends AbstractPostgresBaseClass {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        taskListRepository.deleteAll();
        appUserRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Test
    @Transactional
    void aliasBugPostgreSQL() {
        // Arrange — same setup as the H2 test
        Project project = projectRepository.save(
                new Project("Test Project", "For alias bug demo", LocalDateTime.now(), false));

        TaskList taskList = taskListRepository.save(
                new TaskList(project, "Sprint 1", LocalDateTime.now()));

        AppUser user = appUserRepository.save(
                new AppUser("testuser1", "Jane", "Doe", "jane@example.com",
                        "Password1!", LocalDateTime.now()));

        taskRepository.save(new Task(taskList, "Fix login bug", "desc",
                user, user, LocalDateTime.now().plusDays(3)));
        taskRepository.save(new Task(taskList, "Fix login bug", "desc",
                user, user, LocalDateTime.now().plusDays(5)));
        taskRepository.save(new Task(taskList, "Write tests", "desc",
                user, user, LocalDateTime.now().plusDays(7)));
        taskRepository.flush();

         int updated = taskRepository.updateTaskTitleNativeBuggy(
                "Fix login bug [DONE]", "Fix login bug");
        assertThat(taskRepository.findByTitleContainingIgnoreCase("Fix login bug [DONE]")).hasSize(2);
        assertThat(taskRepository.findByTitleContainingIgnoreCase("Write tests")).hasSize(1);

        // if we want the test to pass, comment above and uncomment this below:

        // assertThatThrownBy(() ->
        //         taskRepository.updateTaskTitleNativeBuggy(
        //                 "Fix login bug [DONE]", "Fix login bug")
        // ).isInstanceOf(InvalidDataAccessResourceUsageException.class);
    }
}
