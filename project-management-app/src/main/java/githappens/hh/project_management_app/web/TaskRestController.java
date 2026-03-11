package githappens.hh.project_management_app.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import githappens.hh.project_management_app.domain.Task;
import githappens.hh.project_management_app.domain.TaskList;
import githappens.hh.project_management_app.domain.TaskListRepository;
import githappens.hh.project_management_app.domain.TaskRepository;
import java.util.List;

@RestController
public class TaskRestController {
    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    public TaskRestController(TaskRepository taskRepository, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }

    @GetMapping("/api/projects/{projectId}/tasklists/{taskListId}/tasks")
    public List<Task> getTasksForTaskList(@PathVariable Long projectId, @PathVariable Long taskListId) {
        return taskRepository.findByTaskList_TaskListId(taskListId);
    }

    @GetMapping("/api/projects/{projectId}/tasklists/{taskListId}/tasks/{taskId}")
    public Task getTaskById(@PathVariable Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    @PostMapping("/api/projects/{projectId}/tasklists/{taskListId}/tasks")
    public Task createTask(@PathVariable Long projectId, @PathVariable Long taskListId, @RequestBody Task task) {
        TaskList taskList = taskListRepository.findById(taskListId).orElse(null);
               
        task.setTaskList(taskList);
        return taskRepository.save(task);
    }

    @DeleteMapping("/api/projects/{projectId}/tasklists/{taskListId}/tasks/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        taskRepository.deleteById(taskId);
    }
}