package githappens.hh.project_management_app.web;

import org.springframework.web.bind.annotation.RestController;
import githappens.hh.project_management_app.domain.CommentRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import githappens.hh.project_management_app.domain.Comment;


@RestController
public class CommentRestController {
    private final CommentRepository commentRepository;

    public CommentRestController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @GetMapping("/api/{projectId}/{tasklistId}/{taskId}/comments")
    public List<Comment> getComments(@PathVariable Long taskId) {
        return commentRepository.findAllByOrderByCreatedAtDesc();
    }
    


}
