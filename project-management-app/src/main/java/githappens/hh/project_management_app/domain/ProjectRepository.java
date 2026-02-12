package githappens.hh.project_management_app.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // find project by name
    Optional<Project> findByName(String name);

     // search project by name (with a keyword)
    Iterable<Project> findByNameContainingIgnoreCase(String keyword);

    // find all projects of a given assigned user
    Iterable<Project> findByAssignedUser(AppUser user);

     // all projects in alphabetical order
    Iterable<Project> findAllByOrderByNameAsc();

     // count an existing users projects
    long countByAssignedUser(AppUser user);

}
