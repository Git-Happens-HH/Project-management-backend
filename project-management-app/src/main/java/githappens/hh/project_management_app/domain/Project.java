package githappens.hh.project_management_app.domain;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    private String title;
    private String description;
    private LocalDateTime createdAt = LocalDateTime.now();
    // JSONIgnore properties
    private List<User> projectMembers;
    // JSONIgnore properties
    private List<TaskList> taskList;


    public Project() {
    }

    public Project(String title, String description, LocalDateTime createdAt, List<User> projectMembers,
            List<TaskList> taskList) {
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.projectMembers = projectMembers;
        this.taskList = taskList;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<User> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(List<User> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public List<TaskList> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskList> taskList) {
        this.taskList = taskList;
    }

    @Override
    public String toString() {
        return "Project [title=" + title + ", description=" + description + ", createdAt="
                + createdAt + "]";
    }

}
