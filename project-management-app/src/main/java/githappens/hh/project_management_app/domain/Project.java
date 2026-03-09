package githappens.hh.project_management_app.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

@Entity(name= "project")
public class Project {

    //projectId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false, updatable = false)
    private Long projectId;

    // title
    @Column(name = "title", nullable = false, updatable = true)
    private String title;

    // description
    @Column(name = "description", nullable = true, updatable = true)
    private String description;

    // createdAt
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // projectMembers
    @OneToMany(mappedBy = "projects", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("projects")
    private List<AppUser> projectMembers = new ArrayList<>();

    //taskList
    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private List<TaskList> taskList = new ArrayList<>();

// CONSTRUCTORS

    public Project() {
    }

    public Project(String title, String description, LocalDateTime createdAt) {
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
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

    public List<AppUser> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(List<AppUser> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public List<TaskList> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskList> taskList) {
        this.taskList = taskList;
    }

// TO STRING

    @Override
    public String toString() {
        return "Project [title=" + title + ", description=" + description + ", createdAt="
                + createdAt + "]";
    }

}
