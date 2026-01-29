package githappens.hh.project_management_app.domain;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    private TaskList taskList;
    private AppUser assignedUser;
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
