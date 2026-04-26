package githappens.hh.project_management_app.RepositoryTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import githappens.hh.project_management_app.domain.TaskList;
import githappens.hh.project_management_app.domain.TaskListRepository;
import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.domain.ProjectRepository;
import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@SpringBootTest
public class TaskListRepositoryTests {

    @Autowired
    private TaskListRepository taskListRepository;
    @Autowired
    private ProjectRepository projectRepository;

    // CREATE a new task list test
    @Test
    public void createNewTaskList() {
        Project project1 = new Project("Test Project1", "Description", LocalDateTime.now(), false);
        projectRepository.save(project1);
        TaskList taskList1 = new TaskList(project1, "Test TaskList1", LocalDateTime.now());
        taskListRepository.save(taskList1);
        assertThat(taskList1.getTaskListId()).isNotNull();
        assertThat(taskList1.getTitle()).isEqualTo("Test TaskList1");
        assertThat(taskList1.getProject().getProjectId()).isEqualTo(project1.getProjectId());
    }

    // SEARCH task list by title test
    @Test
    public void findByTitleShouldReturnTaskList() {
        Project project2 = new Project("Test Project2", "Description", LocalDateTime.now(), false);
        projectRepository.save(project2);
        TaskList taskList2 = new TaskList(project2, "Test TaskList2", LocalDateTime.now());
        taskListRepository.save(taskList2);
        Optional<TaskList> found1 = taskListRepository.findByTitle("Test TaskList2");
        assertThat(found1).isPresent();
        assertThat(found1.get().getTitle()).isEqualTo("Test TaskList2");
        assertThat(found1.get().getProject().getProjectId()).isEqualTo(project2.getProjectId());
    }

    // SEARCH task list by id test
    @Test
    public void findByIdShouldReturnTaskList() {
        Project project3 = new Project("Test Project3", "Description", LocalDateTime.now(), false);
        projectRepository.save(project3);
        TaskList taskList3 = new TaskList(project3, "Test TaskList3", LocalDateTime.now());
        taskListRepository.save(taskList3);
        Long taskListId = taskList3.getTaskListId();
        Optional<TaskList> found2 = taskListRepository.findById(taskListId);
        assertThat(found2).isPresent();
        assertThat(found2.get().getTaskListId()).isEqualTo(taskListId);
        assertThat(found2.get().getTitle()).isEqualTo("Test TaskList3");
        assertThat(found2.get().getProject().getProjectId()).isEqualTo(project3.getProjectId());
    }

    // DELETE task list by Id test
    @Test
    public void deleteTaskListById() {
        Project project4 = new Project("Test Project4", "Description", LocalDateTime.now(), false);
        projectRepository.save(project4);
        TaskList taskList4 = new TaskList(project4, "Test TaskList4", LocalDateTime.now());
        taskListRepository.save(taskList4);
        Long taskListId = taskList4.getTaskListId();
        taskListRepository.deleteById(taskListId);
        Optional<TaskList> deleted = taskListRepository.findById(taskListId);
        assertThat(deleted).isEmpty();
    }

    // CUSTOM QUERY: FIND BY PROJECT ID
    @Test
    public void findByProject_ProjectIdShouldReturnTaskLists() {
        Project project5 = new Project("Test Project5", "Description", LocalDateTime.now(), false);
        projectRepository.save(project5);
        TaskList taskList1 = new TaskList(project5, "TaskList One", LocalDateTime.now());
        TaskList taskList2 = new TaskList(project5, "TaskList Two", LocalDateTime.now());
        taskListRepository.save(taskList1);
        taskListRepository.save(taskList2);

        List<TaskList> found = taskListRepository.findByProject_ProjectId(project5.getProjectId());
        assertThat(found).hasSize(2);
        assertThat(found).extracting(TaskList::getTitle).contains("TaskList One", "TaskList Two");
    }
}