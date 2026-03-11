package githappens.hh.project_management_app.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import githappens.hh.project_management_app.domain.Task;
import githappens.hh.project_management_app.domain.TaskRepository;

@RestController
public class TaskRestController {
    private final TaskRepository taskRepository;

    public TaskRestController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/api/projects/{projectId}/tasklists/{taskListId}/tasks")
    public java.util.List<Task> getTasksForTaskList(@PathVariable Long projectId, @PathVariable Long taskListId) {
        return taskRepository.findByTaskList_TaskListId(taskListId);
    }

    @GetMapping("/api/projects/{projectId}/tasklists/{taskListId}/tasks/{taskId}")
    public Task getTaskById(@PathVariable Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    @PostMapping("/api/projects/{projectId}/tasklists/{taskListId}/tasks")
    public Task createTask(@PathVariable Long projectId, @PathVariable Long taskListId, @RequestBody Task task) {
        return taskRepository.save(task);
    }

    @DeleteMapping("/api/projects/{projectId}/tasklists/{taskListId}/tasks/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        taskRepository.deleteById(taskId);
    }
}