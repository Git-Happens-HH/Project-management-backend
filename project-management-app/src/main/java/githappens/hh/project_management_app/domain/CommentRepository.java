package githappens.hh.project_management_app.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

List<Comment> findAllByOrderByCreatedAtDesc();

List<Comment> findByTaskIdOrderByCreatedAtDesc(Long taskId);

}