package githappens.hh.project_management_app.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.EnumProjectRole;
import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.domain.ProjectRepository;
import githappens.hh.project_management_app.domain.UserProject;
import githappens.hh.project_management_app.domain.UserProjectRepository;

@RestController
public class ProjectRestController {
    
    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final CurrentUserService currentUserService;
    private final ProjectWebSocketController projectWebSocketController;

    public ProjectRestController(ProjectRepository projectRepository, UserProjectRepository userProjectRepository,
            CurrentUserService currentUserService, ProjectWebSocketController projectWebSocketController) {
        this.projectRepository = projectRepository;
        this.userProjectRepository = userProjectRepository;
        this.currentUserService = currentUserService;
        this.projectWebSocketController = projectWebSocketController;
    }


    // // get projects
    // @GetMapping("/api/projects")
    // public @ResponseBody List<Project> listProjects() {
    //     return (List<Project>) projectRepository.findAll();
    // }

    // get project by id
    @GetMapping("/api/projects/{projectId}")
    public @ResponseBody Optional<Project> getProjectById(@PathVariable Long projectId) {
        return projectRepository.findById(projectId);
    }

    // CREATE project
    @PostMapping("/api/projects")
    public @ResponseBody Project createProject(@RequestBody Project project) {
        AppUser requester = currentUserService.getRequester();

        Project savedProject = projectRepository.save(project);

        UserProject ownerMembership = new UserProject();
        ownerMembership.setProject(savedProject);
        ownerMembership.setAppUser(requester);
        ownerMembership.setRole(EnumProjectRole.owner);
        ownerMembership.setJoinedAt(LocalDateTime.now());
        userProjectRepository.save(ownerMembership);

        projectRepository.flush();
        //projectWebSocketController.broadcast

        return savedProject;
    }

    // DELETE project
    @DeleteMapping("/api/projects/{projectId}")
    public void deleteProject(@PathVariable Long projectId) {
        AppUser requester = currentUserService.getRequester();
        UserProject userProject = userProjectRepository.findUserProjectByUserIdAndProjectId(requester.getAppUserId(), projectId);
        EnumProjectRole role = userProject.getRole();
        if (role.equals(EnumProjectRole.owner)) {
            projectRepository.deleteById(projectId);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the project owner can delete the project");
        }
    }

    // EDIT project


     // SAVE edited task
    // @PostMapping("/api/projects/{projectId}/tasklists/{taskListId}/tasks/{taskId}")
    // public Task saveEditedTask(
    //         @PathVariable Long projectId,
    //         @PathVariable Long taskListId,
    //         @PathVariable Long taskId,
    //         @RequestBody Task task) {

    //     Task existingTask = taskRepository.findById(taskId).orElse(null);

    //     TaskList taskList = taskListRepository.findById(taskListId).orElse(null);

    //     // update only editable fields
    //     existingTask.setTitle(task.getTitle());
    //     existingTask.setDescription(task.getDescription());
    //     existingTask.setDeadline(task.getDeadline());
    //     // existingTask.setAssignedUser(task.getAssignedUser());

    //     existingTask.setTaskList(taskList);

    //     Task saved = taskRepository.save(existingTask);
    //     taskRepository.flush();

    //     realtimeService.broadcastTaskLists(projectId);

    //     return saved;
    // }


    

// __________________________________________________________________________________________

    // get projects where user is owner or member
    // @GetMapping("/api/projectsbyuser/{userId}/{role}")
    // public @ResponseBody List<Project> getProjectsByUserIdAndRole(@PathVariable Long userId, @PathVariable EnumProjectRole role) {
    //     return userProjectRepository.findProjectsByUserIdAndRole(userId, role);
    // }

    @GetMapping("/api/projects")
    public List<ProjectResponse> getProjectsForUser() {
    Long userId = currentUserService.getRequesterUserId();

    List<UserProject> memberships = userProjectRepository.findByAppUser_AppUserId(userId);

    return memberships.stream()
            .map(m -> new ProjectResponse(
                    m.getProject().getProjectId(),
                    m.getProject().getTitle(),
                    m.getProject().getDescription(),
                    m.getProject().getCreatedAt(),
                    m.getRole()))
            .toList();
    }

    // List<UserProject> findByProject_ProjectId(Long projectId);

    // Long appUserId,
    // String username,
    // EnumProjectRole role

    // GET ALL MEMBERS OF A PROJECT
    
    @GetMapping("/api/projects/{projectId}/members")
    public List<MemberResponse> getMembers(@PathVariable Long projectId) {

        List<UserProject> members = userProjectRepository.findByProject_ProjectId(projectId);

        return members.stream()
            .map(m -> new MemberResponse(
                m.getAppUser().getAppUserId(),
                m.getAppUser().getUsername(), 
                m.getRole()))
            .toList();
    }
}