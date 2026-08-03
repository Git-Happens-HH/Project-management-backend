package githappens.hh.project_management_app.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import githappens.hh.project_management_app.domain.TaskListRepository;

import java.util.UUID;

@Controller
public class ProjectWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final TaskListRepository taskListRepository;

    public ProjectWebSocketController(SimpMessagingTemplate messagingTemplate,
                                      TaskListRepository taskListRepository) {
        this.messagingTemplate = messagingTemplate;
        this.taskListRepository = taskListRepository;
    }

    @MessageMapping("/project/{projectId}")
    public void sendInitial(@DestinationVariable UUID projectId) {
        messagingTemplate.convertAndSend(
                "/topic/project/" + projectId,
                taskListRepository.findByProject_ProjectId(projectId)
        );
    }
}
