package githappens.hh.project_management_app.domain;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    private AppUser commenter;
    private String content;
    private Task task;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Comment(AppUser commenter, String content, Task task, LocalDateTime createdAt) {
        this.commenter = commenter;
        this.content = content;
        this.task = task;
        this.createdAt = createdAt;
    }

    public Comment() {
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public AppUser getCommenter() {
        return commenter;
    }

    public void setCommenter(AppUser commenter) {
        this.commenter = commenter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Comment [commenter=" + commenter + ", content=" + content + ", task=" + task + ", createdAt="
                + createdAt + "]";
    }

}
