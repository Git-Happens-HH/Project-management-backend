package githappens.hh.project_management_app.domain;

import java.time.LocalDateTime;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;

@Entity(name= "task_list")
public class TaskList {

    // taskListId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_list_id", nullable = false, updatable = false)
    private Long taskListId;

    // project
    @ManyToOne
    @JsonIgnoreProperties("taskList")
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    // title
    @Column(name = "title", nullable = false)
    @NotBlank(message = "Title required")
    private String title;

    // positionNumber
    @Column(name = "position_num")
    private int positionNumber; // Is this neccessary?

    // createdAt
    @Column(name = "created_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // yyyy-MM-dd'T'HH:mm
    private LocalDateTime createdAt;

    // tasks
    @OneToMany(mappedBy = "task_list", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("taskList")
    private List<Task> tasks;

    public TaskList() {
    }

    public TaskList(Project project, String title, int positionNumber, LocalDateTime createdAt, List<Task> tasks) {
        this.project = project;
        this.title = title;
        this.positionNumber = positionNumber;
        this.createdAt = createdAt;
        this.tasks = tasks;
    }

    public Long getTaskListId() {
        return taskListId;
    }

    public void setTaskListId(Long taskListId) {
        this.taskListId = taskListId;
    }

    public Project getProject() {
        return project;
    }

    public void setProjectIdLong(Project project) {
        this.project = project;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPositionNumber() {
        return positionNumber;
    }

    public void setPositionNumber(int positionNumber) {
        this.positionNumber = positionNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "TaskList [project=" + project + ", title=" + title + ", positionNumber=" + positionNumber
                + ", createdAt=" + createdAt + "]";
    }

}
