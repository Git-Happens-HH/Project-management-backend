package githappens.hh.project_management_app.web;

import githappens.hh.project_management_app.service.ProjectRealtimeService;
import org.springframework.web.bind.annotation.RestController;
import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.domain.ProjectRepository;
import githappens.hh.project_management_app.domain.TaskList;
import githappens.hh.project_management_app.domain.TaskListRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
public class TaskListRestController {
    private final TaskListRepository taskListRepository;
    private final ProjectRepository projectRepository;
    private final ProjectRealtimeService realtimeService;

    public TaskListRestController(TaskListRepository taskListRepository, ProjectRepository projectRepository,  ProjectRealtimeService realtimeService) {
        this.taskListRepository = taskListRepository;
        this.projectRepository = projectRepository;
        this.realtimeService =  realtimeService;
    }

    @GetMapping("/api/projects/{projectId}/tasklists")
    public List<TaskList> getTaskListsForProject(@PathVariable Long projectId) {
        return taskListRepository.findByProject_ProjectId(projectId);
    }

    @GetMapping("/api/projects/{projectId}/tasklists/{taskListId}")
    public TaskList getTaskListById(@PathVariable Long taskListId) {
        return taskListRepository.findById(taskListId).orElse(null);
    }

    @PostMapping("/api/projects/{projectId}/tasklists")
    public TaskList createTaskList(@PathVariable Long projectId, @RequestBody TaskList taskList) {
        Project project = projectRepository.findById(projectId).orElse(null);
        taskList.setProject(project);
        TaskList saved = taskListRepository.save(taskList);
        taskListRepository.flush();
        realtimeService.broadcastTaskLists(projectId);
        return saved;
    }
    
    @DeleteMapping("/api/projects/{projectId}/tasklists/{taskListId}")
    public void deleteTaskList(@PathVariable Long taskListId, @PathVariable Long projectId) {
        taskListRepository.deleteById(taskListId);
        taskListRepository.flush();
        realtimeService.broadcastTaskLists(projectId);
    }
}
