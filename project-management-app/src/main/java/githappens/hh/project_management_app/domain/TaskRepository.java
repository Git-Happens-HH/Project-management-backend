package githappens.hh.project_management_app.domain;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
// find task by title
    Optional<Task> findByTitle(String title);

    //_________________________________________________________
    // OTHER POTENTIALLY USEFUL METHODS 

    // search task by title (with a keyword)
    Iterable<Task> findByTitleContainingIgnoreCase(String keyword);

    // find task by id
    Optional<Task> findById(long taskId);

    // find task by duedate
    Optional<Task> findByDueDateTime(LocalDateTime dueDateTime);

    // find all tasks of a given assigned user
    Iterable<Task> findByAssignedUser(AppUser user);


}
