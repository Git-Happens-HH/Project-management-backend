package githappens.hh.project_management_app.web;

import java.util.List;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import githappens.hh.project_management_app.domain.TaskList;
import githappens.hh.project_management_app.domain.TaskListRepository;

@Controller
public class TaskListWebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(TaskListWebSocketController.class);
    private TaskListRepository taskListRepository;

    public TaskListWebSocketController(TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    @MessageMapping("/getTasklists")
    @SendTo("/topic/tasklists")
    public List<TaskList> getTasklists(@Payload Long projectId) {
        logger.info("Received projectId: {}", projectId);
        List<TaskList> tasklists = taskListRepository.findByProject_ProjectId(projectId);
        logger.info("Found {} tasklists for projectId: {}", tasklists.size(), projectId);
        return tasklists;
    }
}