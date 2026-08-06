package githappens.hh.project_management_app.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import githappens.hh.project_management_app.domain.TaskListRepository;
import githappens.hh.project_management_app.domain.UserProjectRepository;

@Controller
public class ProjectWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final TaskListRepository taskListRepository;
    private final UserProjectRepository userProjectRepository;

    public ProjectWebSocketController(SimpMessagingTemplate messagingTemplate,
                                      TaskListRepository taskListRepository, UserProjectRepository userProjectRepository) {
        this.messagingTemplate = messagingTemplate;
        this.taskListRepository = taskListRepository;
        this.userProjectRepository = userProjectRepository;
    }

    @MessageMapping("/project/{projectId}")
    public void sendInitial(@DestinationVariable Long projectId) {
        messagingTemplate.convertAndSend(
                "/topic/project/" + projectId,
                taskListRepository.findByProject_ProjectId(projectId)
        );
    }

}
