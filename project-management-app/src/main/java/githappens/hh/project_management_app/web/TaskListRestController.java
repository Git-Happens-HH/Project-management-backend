package githappens.hh.project_management_app.web;

import org.springframework.web.bind.annotation.RestController;
import githappens.hh.project_management_app.domain.TaskList;
import githappens.hh.project_management_app.domain.TaskListRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class TaskListRestController {
    private final TaskListRepository taskListRepository;

    public TaskListRestController(TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    @GetMapping("/api/projects/{projectId}/tasklists")
    public java.util.List<TaskList> getTaskListsForProject(@PathVariable Long projectId) {
        return taskListRepository.findByProject_ProjectId(projectId);
    }

    @GetMapping("/api/projects/{projectId}/tasklists/{taskListId}")
    public TaskList getTaskListById(@PathVariable Long taskListId) {
        return taskListRepository.findById(taskListId).orElse(null);
    }

    @PostMapping("/api/projects/{projectId}/tasklists")
    public TaskList createTaskList(@PathVariable Long projectId, @RequestBody TaskList taskList) {
        return taskListRepository.save(taskList);
    }
    
    @DeleteMapping("/api/projects/{projectId}/tasklists/{taskListId}")
    public void deleteTaskList(@PathVariable Long taskListId) {
        taskListRepository.deleteById(taskListId);
    }

}
