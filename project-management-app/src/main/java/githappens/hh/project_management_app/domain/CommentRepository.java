package githappens.hh.project_management_app.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDateTime;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    // list of comments ordered by 'created at' date ( desc = newest comment first on the list)
   List<Comment> findAllByOrderByCreatedAtDesc();



}
