package githappens.hh.project_management_app.web;

import org.springframework.web.bind.annotation.RestController;
import githappens.hh.project_management_app.domain.TaskList;
import githappens.hh.project_management_app.domain.TaskListRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

}
