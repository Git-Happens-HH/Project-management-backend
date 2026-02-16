package githappens.hh.project_management_app.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity(name= "comment")
public class Comment {

    // commentId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false, updatable = false)
    private Long commentId;

    // commenter
    @Column(name = "commenter", nullable = false, updatable = false)
    private AppUser commenter;

    // content
    @Column(name = "content", updatable = true)
    @NotBlank(message = "Comment can't be empty")
    private String content;

    //createdAt
    @Column(name = "created_at")
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
