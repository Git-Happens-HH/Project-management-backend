package githappens.hh.project_management_app.domain;

//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectRepository extends JpaRepository<UserProject, UserProjectKey> {

    // find tasklist by title
}
