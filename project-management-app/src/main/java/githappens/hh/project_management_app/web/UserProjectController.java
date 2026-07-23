package githappens.hh.project_management_app.web;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    
    // ADD member to project

    @PostMapping("/api/projects/{projectId}/members/{userId}")
    public void addMemberToProject(@PathVariable Long projectId, @PathVariable Long userId) {
        UserProject userProject = userProjectRepository.findUserProjectByUserIdAndProjectId(userId, projectId);
        
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException("Project Not found"));
        AppUser newUser = appUserRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User Not found"));

        UserProject membership = new UserProject();
        membership.setProject(project);
        membership.setAppUser(newUser);
        membership.setRole(EnumProjectRole.member);

        UserProject saved = userProjectRepository.save(membership);

    }


    // DELETE member from project
    
    @DeleteMapping("api/projects/{projectId}/members/{userId}")
    public void deleteMemberFromProject(@PathVariable Long projectId, 
                                        @PathVariable Long userId, 
                                        @RequestBody AppUser requesterUser) {
        UserProject userProjectToBeDeleted = userProjectRepository.findUserProjectByUserIdAndProjectId(userId, projectId);
        Long requesterUserId = requesterUser.getAppUserId();
        UserProject requesterUserProject = userProjectRepository.findUserProjectByUserIdAndProjectId(requesterUserId, projectId);
        EnumProjectRole requesterUsersRole = requesterUserProject.getRole();
        
        if (userProjectToBeDeleted != null ) {
            if ((requesterUserId == userId && requesterUsersRole == EnumProjectRole.member) 
                || (requesterUserId != userId && requesterUsersRole == EnumProjectRole.owner)) {
                    userProjectRepository.delete(userProjectToBeDeleted);
            }
            else {
                throw new IllegalArgumentException("You do not have the permission to delete this user");
            }

           
        }
    }
    
    


    
}