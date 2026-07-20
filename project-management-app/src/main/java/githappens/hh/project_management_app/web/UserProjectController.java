import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.EnumProjectRole;
import githappens.hh.project_management_app.domain.UserProject;

import githappens.hh.project_management_app.domain.UserProjectRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class UserProjectController {

    private final UserProjectRepository userProjectRepository;

    public UserProjectController(UserProjectRepository userProjectRepository) {
        this.userProjectRepository = userProjectRepository;
    }
    
    // ADD member to project

    @PostMapping("/api/projects/{projectId}/members/{userId}")
    public void addMemberToProject(@PathVariable Long projectId, @PathVariable Long userId) {
        UserProject userProject = userProjectRepository.findUserProjectByUserIdAndProjectId(userId, projectId);
        
        UserProject membership = new UserProject();
        

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

           
        }
    }
    
    


    
}