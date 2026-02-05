package githappens.hh.project_management_app.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    // used by spring security for authentication
    Optional<AppUser> findByUserName(String username);

    // search user by username or email
    List<AppUser> findByUsernameContainingIgnoreCase(String keyword);

    // return users with a specific role (user or admin)
    List<AppUser> findByRole(String role);

}
