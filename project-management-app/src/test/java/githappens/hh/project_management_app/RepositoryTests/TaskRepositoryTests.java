package githappens.hh.project_management_app.RepositoryTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import githappens.hh.project_management_app.domain.Task;
import githappens.hh.project_management_app.domain.TaskRepository;
import githappens.hh.project_management_app.domain.TaskList;
import githappens.hh.project_management_app.domain.TaskListRepository;
import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.domain.ProjectRepository;
import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.AppUserRepository;
import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@SpringBootTest
public class TaskRepositoryTests {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskListRepository taskListRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    // CREATE a new task test
    @Test
    public void createNewTask() {
        AppUser user1 = new AppUser("test1", "Test", "User", "test1@hh.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user1);
        Project project1 = new Project("Test Project1", "Description", LocalDateTime.now(), false);
        projectRepository.save(project1);
        TaskList taskList1 = new TaskList(project1, "Test TaskList1", LocalDateTime.now());
        taskListRepository.save(taskList1);
        Task task1 = new Task(taskList1, "Test Task1", "Testing task creation1", user1, user1, LocalDateTime.now().plusDays(1));
        taskRepository.save(task1);
        assertThat(task1.getTaskId()).isNotNull();
        assertThat(task1.getTitle()).isEqualTo("Test Task1");
        assertThat(task1.getDescription()).isEqualTo("Testing task creation1");
        assertThat(task1.getAssignedUser().getAppUserId()).isEqualTo(user1.getAppUserId());
    }

    // SEARCH task by title test
    @Test
    public void findByTitleShouldReturnTask() {
        AppUser user2 = new AppUser("test2", "Test", "User", "test2@hh.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user2);
        Project project2 = new Project("Test Project2", "Description", LocalDateTime.now(), false);
        projectRepository.save(project2);
        TaskList taskList2 = new TaskList(project2, "Test TaskList2", LocalDateTime.now());
        taskListRepository.save(taskList2);
        Task task2 = new Task(taskList2, "Test Task2", "Testing task search2", user2, user2, LocalDateTime.now().plusDays(1));
        taskRepository.save(task2);
        Optional<Task> found1 = taskRepository.findByTitle("Test Task2");
        assertThat(found1).isPresent();
        assertThat(found1.get().getTitle()).isEqualTo("Test Task2");
        assertThat(found1.get().getDescription()).isEqualTo("Testing task search2");
        assertThat(found1.get().getAssignedUser().getAppUserId()).isEqualTo(user2.getAppUserId());
    }

    // SEARCH task by id test
    @Test
    public void findByIdShouldReturnTask() {
        AppUser user3 = new AppUser("test3", "Test", "User", "test3@hh.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user3);
        Project project3 = new Project("Test Project3", "Description", LocalDateTime.now(), false);
        projectRepository.save(project3);
        TaskList taskList3 = new TaskList(project3, "Test TaskList3", LocalDateTime.now());
        taskListRepository.save(taskList3);
        Task task3 = new Task(taskList3, "Test Task3", "Testing task search by id3", user3, user3, LocalDateTime.now().plusDays(1));
        taskRepository.save(task3);
        Long taskId = task3.getTaskId();
        Optional<Task> found2 = taskRepository.findById(taskId);
        assertThat(found2).isPresent();
        assertThat(found2.get().getTaskId()).isEqualTo(taskId);
        assertThat(found2.get().getTitle()).isEqualTo("Test Task3");
        assertThat(found2.get().getDescription()).isEqualTo("Testing task search by id3");
        assertThat(found2.get().getAssignedUser().getAppUserId()).isEqualTo(user3.getAppUserId());
    }

    // DELETE task by Id test
    @Test
    public void deleteTaskById() {
        AppUser user4 = new AppUser("test4", "Test", "User", "test4@hh.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user4);
        Project project4 = new Project("Test Project4", "Description", LocalDateTime.now(), false);
        projectRepository.save(project4);
        TaskList taskList4 = new TaskList(project4, "Test TaskList4", LocalDateTime.now());
        taskListRepository.save(taskList4);
        Task task4 = new Task(taskList4, "Test Task4", "Testing task deletion4", user4, user4, LocalDateTime.now().plusDays(1));
        taskRepository.save(task4);
        Long taskId = task4.getTaskId();
        taskRepository.deleteById(taskId);
        Optional<Task> deleted = taskRepository.findById(taskId);
        assertThat(deleted).isEmpty();
    }

    // CUSTOM QUERY: FIND BY TITLE ignore case
    @Test
    public void findByTitleContainingIgnoreCaseShouldReturnTasks() {
        AppUser user5 = new AppUser("test5", "Test", "User", "test5@hh.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user5);
        Project project5 = new Project("Test Project5", "Description", LocalDateTime.now(), false);
        projectRepository.save(project5);
        TaskList taskList5 = new TaskList(project5, "Test TaskList5", LocalDateTime.now());
        taskListRepository.save(taskList5);
        Task task1 = new Task(taskList5, "Test Task One", "Description1", user5, user5, LocalDateTime.now().plusDays(1));
        Task task2 = new Task(taskList5, "Another Test Task", "Description2", user5, user5, LocalDateTime.now().plusDays(1));
        taskRepository.save(task1);
        taskRepository.save(task2);

        List<Task> found = taskRepository.findByTitleContainingIgnoreCase("test");
        assertThat(found).hasSize(2);
        assertThat(found).extracting(Task::getTitle).contains("Test Task One", "Another Test Task");
    }

    // CUSTOM QUERY: FIND BY TASKLIST ID
    @Test
    public void findByTaskList_TaskListIdShouldReturnTasks() {
        AppUser user6 = new AppUser("test6", "Test", "User", "test6@hh.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user6);
        Project project6 = new Project("Test Project6", "Description", LocalDateTime.now(), false);
        projectRepository.save(project6);
        TaskList taskList6 = new TaskList(project6, "Test TaskList6", LocalDateTime.now());
        taskListRepository.save(taskList6);
        Task task1 = new Task(taskList6, "Task1", "Description1", user6, user6, LocalDateTime.now().plusDays(1));
        Task task2 = new Task(taskList6, "Task2", "Description2", user6, user6, LocalDateTime.now().plusDays(1));
        taskRepository.save(task1);
        taskRepository.save(task2);

        List<Task> found = taskRepository.findByTaskList_TaskListId(taskList6.getTaskListId());
        assertThat(found).hasSize(2);
        assertThat(found).extracting(Task::getTitle).contains("Task1", "Task2");
    }

    // CUSTOM QUERY: FIND BY DEADLINE
    @Test
    public void findByDeadlineShouldReturnTask() {
        AppUser user7 = new AppUser("test7", "Test", "User", "test7@hh.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user7);
        Project project7 = new Project("Test Project7", "Description", LocalDateTime.now(), false);
        projectRepository.save(project7);
        TaskList taskList7 = new TaskList(project7, "Test TaskList7", LocalDateTime.now());
        taskListRepository.save(taskList7);
        LocalDateTime deadline = LocalDateTime.of(2077, 4, 25, 12, 0);
        Task task = new Task(taskList7, "Task with deadline", "Testing deadline search", user7, user7, deadline);
        taskRepository.save(task);
        Optional<Task> found = taskRepository.findByDeadline(deadline);
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Task with deadline");
    }

    // CUSTOM QUERY: FIND BY ASSIGNED USER
    @Test
    public void findByAssignedUserShouldReturnTasks() {
        AppUser user8 = new AppUser("test8", "Test", "User", "test8@hh.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user8);
        Project project8 = new Project("Test Project8", "Description", LocalDateTime.now(), false);
        projectRepository.save(project8);
        TaskList taskList8 = new TaskList(project8, "Test TaskList8", LocalDateTime.now());
        taskListRepository.save(taskList8);
        Task task1 = new Task(taskList8, "Assigned Task1", "Description1", user8, user8, LocalDateTime.now().plusDays(1));
        Task task2 = new Task(taskList8, "Assigned Task2", "Description2", user8, user8, LocalDateTime.now().plusDays(1));
        taskRepository.save(task1);
        taskRepository.save(task2);

        List<Task> found = (List<Task>) taskRepository.findByAssignedUser(user8);
        assertThat(found).hasSize(2);
        assertThat(found).extracting(Task::getTitle).contains("Assigned Task1", "Assigned Task2");
    }
}