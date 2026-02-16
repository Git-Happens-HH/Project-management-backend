package githappens.hh.project_management_app.domain;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskListId;
    private Project project;
    private String title;
    private int positionNumber; // Is this neccessary?
    private LocalDateTime createdAt;
    // JSON ignore
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

    public void setProject(Project project) {
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
