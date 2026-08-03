package githappens.hh.project_management_app.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

List<Comment> findAllByOrderByCreatedAtDesc();

List<Comment> findByTaskOrderByCreatedAtDesc(Task task);


}