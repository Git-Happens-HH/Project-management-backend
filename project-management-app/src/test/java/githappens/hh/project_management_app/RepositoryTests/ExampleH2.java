package githappens.hh.project_management_app.RepositoryTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.AppUserRepository;
import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.domain.ProjectRepository;
import githappens.hh.project_management_app.domain.Task;
import githappens.hh.project_management_app.domain.TaskList;
import githappens.hh.project_management_app.domain.TaskListRepository;
import githappens.hh.project_management_app.domain.TaskRepository;

// This test class' code has been generated using Claude Sonnet 4.6-language model

@SpringBootTest
@Transactional
public class ExampleH2 {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void aliasBugH2() {
        // Arrange — build the full entity chain: Project → TaskList → AppUser → Tasks
        Project project = projectRepository.save(
                new Project("Test Project", "For alias bug demo", LocalDateTime.now(), false));

        TaskList taskList = taskListRepository.save(
                new TaskList(project, "Sprint 1", LocalDateTime.now()));

        AppUser user = appUserRepository.save(
                new AppUser("testuser1", "Jane", "Doe", "jane@example.com",
                        "Password1!", LocalDateTime.now()));

        // Two tasks with the same title — both should be updated
        taskRepository.save(new Task(taskList, "Fix login bug", "desc",
                user, user, LocalDateTime.now().plusDays(3)));
        taskRepository.save(new Task(taskList, "Fix login bug", "desc",
                user, user, LocalDateTime.now().plusDays(5)));
        // One task with a different title — should NOT be updated
        taskRepository.save(new Task(taskList, "Write tests", "desc",
                user, user, LocalDateTime.now().plusDays(7)));
        taskRepository.flush();

        // Act — buggy native query: SET t.title = :newTitle (alias in SET clause)
        int updated = taskRepository.updateTaskTitleNativeBuggy(
                "Fix login bug [DONE]", "Fix login bug");

        // Assert — H2 silently accepts the alias; test passes
        // This is the danger: the bug is completely hidden during local development
assertThat(taskRepository.findByTitleContainingIgnoreCase("Fix login bug [DONE]")).hasSize(2);
assertThat(taskRepository.findByTitleContainingIgnoreCase("Write tests")).hasSize(1);
    }
}
