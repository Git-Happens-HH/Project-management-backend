package githappens.hh.project_management_app.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {

    // find tasklist by title
    Optional<TaskList> findByTitle(String title);
}
