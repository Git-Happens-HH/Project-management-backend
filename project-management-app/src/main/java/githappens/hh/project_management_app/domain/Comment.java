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
    private Long taskId;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Comment() {
    }

    public Comment(AppUser commenter, String content, LocalDateTime createdAt) {
        this.commenter = commenter;
        this.content = content;
        this.createdAt = createdAt;
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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Comment [commenter=" + commenter + ", content=" + content + ", createdAt=" + createdAt + "]";
    }

}
