package githappens.hh.project_management_app.RepositoryTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import githappens.hh.project_management_app.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("testcontainer")
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class TCTaskRestControllerTests extends AbstractPostgresBaseClass {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskListRepository taskListRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    private Long projectId;
    private Long taskListId;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        taskListRepository.deleteAll();
        projectRepository.deleteAll();
        appUserRepository.deleteAll();

        AppUser user = new AppUser("ctrl1", "Test", "User", "ctrl1@hh.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user);

        Project project = new Project("Test Project", "Description", LocalDateTime.now(), false);
        projectRepository.save(project);
        projectId = project.getProjectId(); 

        TaskList taskList = new TaskList(project, "Test TaskList", LocalDateTime.now());
        taskListRepository.save(taskList);
        taskListId = taskList.getTaskListId(); 

        Task task1 = new Task(taskList, "Task One", "Desc1", user, user, LocalDateTime.now().plusDays(1));
        Task task2 = new Task(taskList, "Task Two", "Desc2", user, user, LocalDateTime.now().plusDays(2));
        taskRepository.save(task1);
        taskRepository.save(task2);
    }

    @Test
    void getTasksForTaskList_returnsAllTasksInList() throws Exception {
        mockMvc.perform(get("/api/projects/{projectId}/tasklists/{taskListId}/tasks",
                        projectId, taskListId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder("Task One", "Task Two")));
    }

    @Test
    void getTasksForTaskList_withWrongTaskListId_returnsEmptyList() throws Exception {
        Long nonExistentTaskListId = 99999L;

        mockMvc.perform(get("/api/projects/{projectId}/tasklists/{taskListId}/tasks",
                        projectId, nonExistentTaskListId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}