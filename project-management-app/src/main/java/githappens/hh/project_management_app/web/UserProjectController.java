package githappens.hh.project_management_app.web;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.AppUserRepository;
import githappens.hh.project_management_app.domain.EnumProjectRole;
import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.domain.ProjectRepository;
import githappens.hh.project_management_app.domain.UserProject;

import githappens.hh.project_management_app.domain.UserProjectRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.persistence.EntityNotFoundException;

@RestController
public class UserProjectController {

    private final UserProjectRepository userProjectRepository;
    private final ProjectRepository projectRepository;
    private final AppUserRepository appUserRepository;
    private final CurrentUserService currentUserService;

    public UserProjectController(UserProjectRepository userProjectRepository, ProjectRepository projectRepository,
            AppUserRepository appUserRepository, CurrentUserService currentUserService) {
        this.userProjectRepository = userProjectRepository;
        this.projectRepository = projectRepository;
        this.appUserRepository = appUserRepository;
        this.currentUserService = currentUserService;
    }

    private EnumProjectRole getRequesterUserRole(Long projectId, Long userId) {
        UserProject requesterUserProject = userProjectRepository.findUserProjectByUserIdAndProjectId(userId, projectId);
        if (requesterUserProject == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a member of this project");
        }
        return requesterUserProject.getRole();
    }

    
    // ADD a member to a project

    @PostMapping("/api/projects/{projectId}/members/{userId}")
    public void addMemberToProject(@PathVariable Long projectId, @PathVariable Long userId) {

        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException("Project Not found"));
        AppUser newMember = appUserRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User Not found"));

        Long requesterUserId = currentUserService.getRequesterUserId();
        EnumProjectRole requesterUserRole = getRequesterUserRole(projectId, requesterUserId);

        if (requesterUserRole == EnumProjectRole.member) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the project owner can add new members");
        } else if (userProjectRepository.findUserProjectByUserIdAndProjectId(userId, projectId) != null) { // if membership already exists
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is already a member of this project");
        }
        
        UserProject membership = new UserProject();
        membership.setProject(project);
        membership.setAppUser(newMember);
        membership.setRole(EnumProjectRole.member);
        membership.setJoinedAt(LocalDateTime.now());

        UserProject saved = userProjectRepository.save(membership);
    }

    
    // DELETE a member from a project
    
    @DeleteMapping("/api/projects/{projectId}/members/{userId}")
    public void deleteMemberFromProject(@PathVariable Long projectId, 
                                        @PathVariable Long userId) {

        // Check if project and user exist
        if (projectRepository.findById(projectId).isEmpty()) {
            throw new EntityNotFoundException("Project Not found");
        } else if (appUserRepository.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("User Not found");
        }

        Long requesterUserId = currentUserService.getRequesterUserId();
        EnumProjectRole requesterUserRole = getRequesterUserRole(projectId, requesterUserId);

        UserProject userProjectToBeDeleted = userProjectRepository.findUserProjectByUserIdAndProjectId(userId, projectId);
        
        if (userProjectToBeDeleted != null ) {
            if ((requesterUserId.equals(userId) && requesterUserRole == EnumProjectRole.member) // if requester is a member trying to delete themselves -> allow
                || (!requesterUserId.equals(userId) && requesterUserRole == EnumProjectRole.owner)) { // if requester is an owner trying to delete a member -> allow
                    userProjectRepository.delete(userProjectToBeDeleted);
            }
            else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this user");
            }       
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "This user cannot be removed from the project because they are not a member");
        }
    }

    
    // PROMOTE a member to owner
    
    @PutMapping("/api/projects/{projectId}/members/{userId}/promote-to-owner")
    public void promoteUserToOwner(@PathVariable Long projectId, 
                                   @PathVariable Long userId) {

        Long requesterUserId = currentUserService.getRequesterUserId();
        EnumProjectRole requesterUserRole = getRequesterUserRole(projectId, requesterUserId);

        if (requesterUserRole != EnumProjectRole.owner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the project owner can promote members to owner");
        }

        UserProject userProject = userProjectRepository.findUserProjectByUserIdAndProjectId(userId, projectId);
        if (userProject == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This user is not a member of the project");
        }
        if (userProject.getRole() == EnumProjectRole.owner) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already an owner of this project");
        }
        
        userProject.setRole(EnumProjectRole.owner);
        userProjectRepository.save(userProject);
        
        UserProject requesterUserProject = userProjectRepository.findUserProjectByUserIdAndProjectId(requesterUserId, projectId);
        requesterUserProject.setRole(EnumProjectRole.member);
        userProjectRepository.save(requesterUserProject);
    }
        
}