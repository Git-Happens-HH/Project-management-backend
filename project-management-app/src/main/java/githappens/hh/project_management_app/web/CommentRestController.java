package githappens.hh.project_management_app.web;

import org.springframework.web.bind.annotation.RestController;
import githappens.hh.project_management_app.domain.CommentRepository;
import githappens.hh.project_management_app.domain.Task;
import githappens.hh.project_management_app.domain.TaskRepository;

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
    private final TaskRepository taskRepository;

    public CommentRestController(CommentRepository commentRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
    }


    @GetMapping("/api/projects/{projectId}/tasklists/{tasklistId}/tasks/{taskId}/comments")
    public List<Comment> getComments(@PathVariable("taskId") Long taskId) {
    return commentRepository.findByTask_TaskIdOrderByCreatedAtDesc(taskId);
    }

    @GetMapping("/api/projects/{projectId}/tasklists/{tasklistId}/tasks/{taskId}/comments/{commentId}")
    public Comment getCommentById(@PathVariable Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    @PostMapping("/api/projects/{projectId}/tasklists/{tasklistId}/tasks/{taskId}/comments")
    public Comment createComment(@PathVariable("taskId") Long taskId, @RequestBody Comment comment) {
    Task task = taskRepository.findById(taskId).orElse(null);
    comment.setTask(task);
    return commentRepository.save(comment);
    }

    @DeleteMapping("/api/projects/{projectId}/tasklists/{tasklistId}/tasks/{taskId}/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentRepository.deleteById(commentId);
    }

}
