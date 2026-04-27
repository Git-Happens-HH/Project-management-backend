package githappens.hh.project_management_app.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;

@Entity(name = "task")
public class Task {

    // taskId, taskList, assignedUser, title, description,
    // createdBy, positionNumber (?), deadline, comments
    

    // taskId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false, updatable = false)
    private Long taskId;

    // taskList
    @ManyToOne(optional = false)
    @JsonIgnoreProperties("tasks")
    @JoinColumn(name = "task_list_id", nullable = true)
    private TaskList taskList;

    // title
    @Column(name = "title", nullable = false, updatable = true)
    private String title;

    // description
    @Column(name = "description", nullable = true, updatable = true)
    private String description;

    // createdBy
    @ManyToOne
    @JsonIgnoreProperties("tasksCreated")
    @JoinColumn(name = "created_by", nullable = true)
    private AppUser createdBy;

    // assignedUser
    @ManyToOne
    @JsonIgnoreProperties("tasksAssigned")
    @JoinColumn(name = "assigned_user", nullable = true, updatable = true)
    private AppUser assignedUser;

    // deadline
    @Column(name="deadline", nullable = false, updatable = true)
    @FutureOrPresent(message = "Due date must be in the present or future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // yyyy-MM-dd'T'HH:mm follows iso-standards, i.e html uses this format
    private LocalDateTime deadline;

    // comments
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("task")
    private List<Comment> comments = new ArrayList<>();


// CONSTRUCTORS

    public Task() {
    }

    public Task(TaskList taskList, String title, String description, AppUser createdBy, AppUser assignedUser, LocalDateTime deadline) {
        this.taskList = taskList;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.assignedUser = assignedUser;
        this.deadline = deadline;

    }

// GETTERS AND SETTERS

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

    public AppUser getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(AppUser assignedUser) {
        this.assignedUser = assignedUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AppUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AppUser createdBy) {
        this.createdBy = createdBy;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    
    public LocalDateTime getDeadline() {
        return deadline;
    }


    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

// TO STRING

    @Override
    public String toString() {
        return "TaskList=" + taskList + ", title=" + title + ", description=" + description
                + ", deadline=" + deadline + ", comments=" + comments + "]";
    }
   
}
