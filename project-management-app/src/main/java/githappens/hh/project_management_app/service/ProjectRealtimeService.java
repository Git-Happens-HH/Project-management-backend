package githappens.hh.project_management_app.service;

import githappens.hh.project_management_app.domain.TaskListRepository;
import githappens.hh.project_management_app.domain.UserProjectRepository;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProjectRealtimeService {

    private final SimpMessagingTemplate messagingTemplate;
    private final TaskListRepository taskListRepository;
    private final UserProjectRepository userProjectRepository;

    public ProjectRealtimeService(SimpMessagingTemplate messagingTemplate, 
                                    TaskListRepository taskListRepository,
                                    UserProjectRepository userProjectRepository) {
        this.messagingTemplate = messagingTemplate;
        this.taskListRepository = taskListRepository;
        this.userProjectRepository = userProjectRepository;

    }

    public void broadcastTaskLists(long projectId){
        var taskLists = taskListRepository.findByProject_ProjectId(projectId);
        messagingTemplate.convertAndSend("/topic/project/" + projectId, taskLists);
    }

}