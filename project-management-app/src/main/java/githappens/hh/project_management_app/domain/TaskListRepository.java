package githappens.hh.project_management_app.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskListRepository extends JpaRepository<TaskList, UUID> {

    // find tasklist by title
    Optional<TaskList> findByTitle(String title);

    // find all task lists that belong to a project (by projectId)
    List<TaskList> findByProject_ProjectId(UUID projectId);

}
