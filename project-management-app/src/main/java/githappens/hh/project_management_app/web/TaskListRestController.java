package githappens.hh.project_management_app.web;

import org.springframework.web.bind.annotation.RestController;

import githappens.hh.project_management_app.domain.TaskListRepository;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TaskListRestController {
    private final TaskListRepository taskListRepository;

    public TaskListRestController(TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    @GetMapping("/api/{projectId}/tasklists")
    public List<TaskList> getAllTaskLists(@PathVariable Long projectId) {
        return taskListRepository.findById(projectId);
    }

}
