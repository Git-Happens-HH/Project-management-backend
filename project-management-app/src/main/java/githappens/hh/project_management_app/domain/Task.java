package githappens.hh.project_management_app.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;

@Entity(name= "task")
public class Task {

    // taskId
    
    @Column(name = "task_id", nullable = false, updatable = false)
    private Long taskId;

    // taskList
    @ManyToOne(optional=false)
    @JsonIgnoreProperties("tasks")
    @JoinColumn(name = "task_list_id", nullable = false)
    private TaskList taskList;

    @ManyToOne
    @JsonIgnoreProperties("tasks")
    @JoinColumn(name = "app_user_id", nullable = true)
    private AppUser assignedUser;

    // createdBy attribute to connect task to creator?

    // positionNum ??

    private String title;

    private String description;
    // Ignore json
    private List<Comment> comments;

    public Task() {
    }

    public Task(TaskList taskList, AppUser assignedUser, String title, String description, List<Comment> comments) {
        this.taskList = taskList;
        this.assignedUser = assignedUser;
        this.title = title;
        this.description = description;
        this.comments = comments;
    }

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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Task [taskList=" + taskList + ", assignedUser=" + assignedUser + ", title=" + title + ", description="
                + description + "]";
    }

}
