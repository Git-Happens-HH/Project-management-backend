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
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        return taskRepository.findByTaskList_TaskListIdOrderBySortOrderAscTaskIdAsc(taskListId);
    }

    // get task by taskId
    @GetMapping("/api/projects/{projectId}/tasklists/{taskListId}/tasks/{taskId}")
    public Task getTaskById(@PathVariable Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "task not found"));
    }

    // CREATE task
    @PostMapping("/api/projects/{projectId}/tasklists/{taskListId}/tasks")
    public Task createTask(@PathVariable Long projectId, @PathVariable Long taskListId, @RequestBody Task task) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "tasklist not found"));
        task.setTaskList(taskList);
        task.setSortOrder(nextSortOrder(taskListId));
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

    @PostMapping("/api/projects/{projectId}/tasklists/{taskListId}/tasks/{taskId}/to/{newTaskListId}")
    public Task moveTask(
            @PathVariable Long projectId,
            @PathVariable Long taskListId,
            @PathVariable Long taskId,
            @PathVariable Long newTaskListId) {

        System.out.println(
                "MOVE ENDPOINT HIT: project=" + projectId +
                        " sourceList=" + taskListId +
                        " task=" + taskId +
                        " targetList=" + newTaskListId);

        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        TaskList newTaskList = taskListRepository.findById(newTaskListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task list not found"));

        existingTask.setTaskList(newTaskList);
        existingTask.setSortOrder(nextSortOrder(newTaskListId));

        Task saved = taskRepository.save(existingTask);
        taskRepository.flush();
        realtimeService.broadcastTaskLists(projectId);
        return saved;
    }

    @PostMapping("/api/projects/{projectId}/tasklists/{taskListId}/task-order")
    public List<Task> reorderTasks(
            @PathVariable Long projectId,
            @PathVariable Long taskListId,
            @RequestBody List<Long> orderedTaskIds) {

        List<Task> reorderedTasks = new ArrayList<>();
        Map<Long, Task> tasksById = taskRepository.findByTaskList_TaskListIdOrderBySortOrderAscTaskIdAsc(taskListId)
                .stream()
                .collect(Collectors.toMap(Task::getTaskId, Function.identity()));

        for (int index = 0; index < orderedTaskIds.size(); index++) {
            Long taskId = orderedTaskIds.get(index);
            Task task = tasksById.get(taskId);

            if (task == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
            }

            if (task.getTaskList() == null || !task.getTaskList().getTaskListId().equals(taskListId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task does not belong to this task list");
            }

            task.setSortOrder(index);
            reorderedTasks.add(task);
        }

        taskRepository.saveAll(reorderedTasks);
        taskRepository.flush();
        realtimeService.broadcastTaskLists(projectId);

        return taskRepository.findByTaskList_TaskListIdOrderBySortOrderAscTaskIdAsc(taskListId);
    }

    private int nextSortOrder(Long taskListId) {
        Integer maxSortOrder = taskRepository.findMaxSortOrderByTaskListId(taskListId);
        return maxSortOrder == null ? 0 : maxSortOrder + 1;
    }
}