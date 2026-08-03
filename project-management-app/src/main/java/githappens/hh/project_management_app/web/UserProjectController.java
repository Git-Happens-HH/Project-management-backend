package githappens.hh.project_management_app.web;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    
    public UserProjectController(UserProjectRepository userProjectRepository, ProjectRepository projectRepository,
            AppUserRepository appUserRepository) {
        this.userProjectRepository = userProjectRepository;
        this.projectRepository = projectRepository;
        this.appUserRepository = appUserRepository;
    }

    private UUID getRequesterUserId(UUID projectId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AppUser requester = appUserRepository.findByEmail(email)
            .orElseThrow(() ->
                new EntityNotFoundException("User Not found"));
        return requester.getAppUserId();
    }

    private EnumProjectRole getRequesterUserRole(UUID projectId, UUID userId) {
        UserProject requesterUserProject = userProjectRepository.findUserProjectByUserIdAndProjectId(userId, projectId);
        if (requesterUserProject == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a member of this project");
        }
        return requesterUserProject.getRole();
    }

    
    // ADD a member to a project

    @PostMapping("/api/projects/{projectId}/members/{userId}")
    public void addMemberToProject(@PathVariable UUID projectId, @PathVariable UUID userId) {

        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException("Project Not found"));
        AppUser newMember = appUserRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User Not found"));

        UUID requesterUserId = getRequesterUserId(projectId);
        EnumProjectRole requesterUserRole = getRequesterUserRole(projectId, requesterUserId);

        if (userProjectRepository.findUserProjectByUserIdAndProjectId(userId, projectId) != null) { // if membership already exists
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is already a member of this project");
        } else if (requesterUserRole == EnumProjectRole.member) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the project owner can add new members");
        }
        
        UserProject membership = new UserProject();
        membership.setProject(project);
        membership.setAppUser(newMember);
        membership.setRole(EnumProjectRole.member);
        //membership.setJoinedAt(LocalDateTime.now());

        UserProject saved = userProjectRepository.save(membership);
    }

    
    // DELETE a member from a project
    
    @DeleteMapping("/api/projects/{projectId}/members/{userId}")
    public void deleteMemberFromProject(@PathVariable UUID projectId, 
                                        @PathVariable UUID userId) {

        // Check if project and user exist
        if (projectRepository.findById(projectId).isEmpty()) {
            throw new EntityNotFoundException("Project Not found");
        } else if (appUserRepository.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("User Not found");
        }

        UUID requesterUserId = getRequesterUserId(projectId);
        EnumProjectRole requesterUserRole = getRequesterUserRole(projectId, requesterUserId);

        UserProject userProjectToBeDeleted = userProjectRepository.findUserProjectByUserIdAndProjectId(userId, projectId);
        
        if (userProjectToBeDeleted != null ) {
            if ((requesterUserId.equals(userId) && requesterUserRole == EnumProjectRole.member) // if requester is a member trying to delete themselves -> allow
                || (requesterUserId != userId && requesterUserRole == EnumProjectRole.owner)) { // if requester is an owner trying to delete a member -> allow
                    userProjectRepository.delete(userProjectToBeDeleted);
            }
            else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this user");
            }       
        } else {
            throw new Error("This user cannot be removed from project because they are not a member");
        }
    }
    
    


    
}