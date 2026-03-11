package githappens.hh.project_management_app.web;

import org.springframework.web.bind.annotation.RestController;
import githappens.hh.project_management_app.domain.CommentRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;
import githappens.hh.project_management_app.domain.Comment;


@RestController
public class CommentRestController {
    private final CommentRepository commentRepository;

    public CommentRestController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @GetMapping("/api/projects/{projectId}/tasklists/{tasklistId}/tasks/{taskId}/comments")
    public List<Comment> getComments(@PathVariable Long taskId) {
    return commentRepository.findByTaskIdOrderByCreatedAtDesc(taskId);
    }

    @GetMapping("/api/projects/{projectId}/tasklists/{tasklistId}/tasks/{taskId}/comments/{commentId}")
    public Comment getCommentById(@PathVariable Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    @PostMapping("/api/projects/{projectId}/tasklists/{tasklistId}/tasks/{taskId}/comments")
    public Comment createComment(@PathVariable Long taskId, @RequestBody Comment comment) {
    comment.setTaskId(taskId);
    return commentRepository.save(comment);
    }

    @DeleteMapping("/api/projects/{projectId}/tasklists/{tasklistId}/tasks/{taskId}/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentRepository.deleteById(commentId);
    }

}
