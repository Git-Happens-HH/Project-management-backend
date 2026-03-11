package githappens.hh.project_management_app.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity(name= "comment")
public class Comment {

    //commentId, commenter, task, content, createdAt

    // commentId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false, updatable = false)
    private Long commentId;

    // commenter
    @ManyToOne(optional=false)
    @JsonIgnoreProperties("comments")
    @JoinColumn(name = "app_user_id")
    private AppUser commenter;

    // task
    @ManyToOne(optional=false)
    @JsonIgnoreProperties("comments")
    @JoinColumn(name = "task_id")
    private Task task;

    // content
    @Column(name = "content", updatable = true)
    @NotBlank(message = "Comment can't be empty")
    private String content;

    // createdAt
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

// CONSTRUCTORS

    public Comment() {
    }

    public Comment(AppUser commenter, Task task, String content, LocalDateTime createdAt) {
        this.commenter = commenter;
        this.task = task;
        this.content = content;
        this.createdAt = createdAt;
    }

// GETTERS AND SETTERS

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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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

// TO STRING

    @Override
    public String toString() {
        return "Comment [commenter=" + commenter.getFirstName() + " " + commenter.getLastName()
        + ", task=" + task.getTitle() + ", content=" + content + ", createdAt=" + createdAt + "]";
    }

}
