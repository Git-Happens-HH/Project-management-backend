package githappens.hh.project_management_app.web;

import java.util.List;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import githappens.hh.project_management_app.domain.TaskList;
import githappens.hh.project_management_app.domain.TaskListRepository;

@Controller
public class TaskListWebSocketController {

    private TaskListRepository taskListRepository;

    public TaskListWebSocketController(TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    @MessageMapping("/getTasklists")
    @SendTo("/topic/tasklists")
    public List<TaskList> getTasklists(Long projectId) {
        return taskListRepository.findByProject_ProjectId(projectId);
    }
}