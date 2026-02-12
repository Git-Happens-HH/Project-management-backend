package githappens.hh.project_management_app.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}