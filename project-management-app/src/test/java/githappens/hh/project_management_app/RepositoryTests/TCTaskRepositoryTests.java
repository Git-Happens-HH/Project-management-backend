package githappens.hh.project_management_app.RepositoryTests;

import githappens.hh.project_management_app.domain.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("testcontainer")
@SpringBootTest
class TCTaskRepositoryTests {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        // registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        taskListRepository.deleteAll();
        projectRepository.deleteAll();
        appUserRepository.deleteAll();
    }

    // CREATE TASK TEST
    @Test
    void createNewTask() {

        AppUser user = new AppUser("test1", "Test", "User","test1@hh.com", "Test123!",LocalDateTime.now());
        appUserRepository.save(user);

        Project project = new Project( "Test Project", "Description",LocalDateTime.now(), false);
        projectRepository.save(project);

        TaskList taskList = new TaskList(project, "TaskList",LocalDateTime.now() );
        taskListRepository.save(taskList);

        Task task = new Task( taskList, "Test Task",  "Testing task creation",  user,  user,  LocalDateTime.now().plusDays(1));
        taskRepository.save(task);

        assertThat(task.getTaskId()).isNotNull();
        assertThat(task.getTitle()).isEqualTo("Test Task");
        assertThat(task.getDescription()).isEqualTo("Testing task creation");
        assertThat(task.getAssignedUser().getAppUserId()).isEqualTo(user.getAppUserId());
    }

    // FIND BY TITLE (IGNORE CASE)
    @Test
    void findByTitleContainingIgnoreCaseShouldReturnTasks() {

        AppUser user = new AppUser("test5", "Test", "User","test5@hh.com", "Test123!",LocalDateTime.now() );
        appUserRepository.save(user);

        Project project = new Project("Test Project","Description", LocalDateTime.now(),false);
        projectRepository.save(project);

        TaskList taskList = new TaskList(project,"TaskList",LocalDateTime.now());
        taskListRepository.save(taskList);

        Task task1 = new Task(taskList,"Test Task One","Description1", user, user, LocalDateTime.now().plusDays(1));
        Task task2 = new Task(taskList,"Another Test Task","Description2", user, user, LocalDateTime.now().plusDays(1));
        taskRepository.save(task1);
        taskRepository.save(task2);

        List<Task> found = taskRepository.findByTitleContainingIgnoreCase("test");

        assertThat(found).hasSize(2);
        assertThat(found).extracting(Task::getTitle).contains("Test Task One", "Another Test Task");
    }
}
