package githappens.hh.project_management_app.web;

import githappens.hh.project_management_app.service.ProjectRealtimeService;
import org.springframework.web.bind.annotation.*;
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
    private final ProjectRealtimeService realtimeService;

    public TaskRestController(TaskRepository taskRepository, TaskListRepository taskListRepository, 
                                ProjectRealtimeService realtimeService) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
        this.realtimeService = realtimeService;
    }

    // get tasks (by tasklist id)
    @GetMapping("/api/projects/{projectId}/tasklists/{taskListId}/tasks")
    public List<Task> getTasksForTaskList(@PathVariable Long projectId, @PathVariable Long taskListId) {
        return taskRepository.findByTaskList_TaskListId(taskListId);
    }

    // get task by taskId
    @GetMapping("/api/projects/{projectId}/tasklists/{taskListId}/tasks/{taskId}")
    public Task getTaskById(@PathVariable Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "task not found"));
    }

    // CREATE task
    @PostMapping("/api/projects/{projectId}/tasklists/{taskListId}/tasks")
    public Task createTask(@PathVariable Long projectId, @PathVariable Long taskListId, @RequestBody Task task) {
        TaskList taskList = taskListRepository.findById(taskListId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "tasklist not found"));
        task.setTaskList(taskList);
        Task saved = taskRepository.save(task);
        taskRepository.flush();
        realtimeService.broadcastTaskLists(projectId);
        return saved;
    }

    // SAVE edited task
    @PostMapping("/api/projects/{projectId}/tasklists/{taskListId}/tasks/{taskId}")
    public Task saveEditedTask(
        @PathVariable Long projectId,
        @PathVariable Long taskListId,
        @PathVariable Long taskId,
        @RequestBody Task task) {

    Task existingTask = taskRepository.findById(taskId).orElse(null);

    TaskList taskList = taskListRepository.findById(taskListId).orElse(null);

    // update only editable fields
    existingTask.setTitle(task.getTitle());
    existingTask.setDescription(task.getDescription());
    existingTask.setDeadline(task.getDeadline());
    // existingTask.setAssignedUser(task.getAssignedUser());

    existingTask.setTaskList(taskList);

    Task saved = taskRepository.save(existingTask);
    taskRepository.flush();

    realtimeService.broadcastTaskLists(projectId);

    return saved;
}
    // DELETE task
    @DeleteMapping("/api/projects/{projectId}/tasklists/{taskListId}/tasks/{taskId}")
    public void deleteTask(@PathVariable Long taskId, @PathVariable Long projectId) {
        taskRepository.deleteById(taskId);
        taskRepository.flush();
        realtimeService.broadcastTaskLists(projectId);
    }
}
