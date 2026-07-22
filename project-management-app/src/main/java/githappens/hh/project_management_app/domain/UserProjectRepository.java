package githappens.hh.project_management_app.domain;

import java.util.List;

//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProjectRepository extends JpaRepository<UserProject, UserProjectKey> {

// FIND PROJECT BY ROLE AND USER (OWNER / MEMBER)
// includes only projects
@Query("SELECT up.project FROM user_project up WHERE up.appUser.appUserId = :userId AND up.role = :role")
List<Project> findProjectsByUserIdAndRole(@Param("userId") Long userId, @Param("role") EnumProjectRole role);

@Query("SELECT up FROM user_project up WHERE up.appUser.appUserId = :userId AND up.project.projectId = :projectId")
    UserProject findUserProjectByUserIdAndProjectId(@Param("userId") Long userId, @Param("projectId") Long projectId);
}

    
