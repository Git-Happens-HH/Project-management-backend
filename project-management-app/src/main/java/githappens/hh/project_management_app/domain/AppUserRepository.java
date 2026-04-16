package githappens.hh.project_management_app.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    // used by spring security for authentication
    Optional<AppUser> findByUsername(String username);

    // search user by username or email
    List<AppUser> findByUsernameContainingIgnoreCase(String keyword);
    Optional<AppUser> findByEmail(String email);

    // check whether email already exists
    boolean existsByEmail(String email);

    // check whether username already exists
    boolean existsByUsername(String username);
}
