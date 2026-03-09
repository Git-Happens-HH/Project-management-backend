package githappens.hh.project_management_app.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // find project by name
    Optional<Project> findByTitle(String title);

     // search project by name (with a keyword)
    Iterable<Project> findByTitleContainingIgnoreCase(String keyword);

    // find all projects of a given assigned user
    // Iterable<Project> findByAssignedUser(AppUser user);

     // all projects in alphabetical order
    Iterable<Project> findAllByOrderByTitleAsc();

     // count an existing users projects
    // long countByAssignedUser(AppUser user);

}
