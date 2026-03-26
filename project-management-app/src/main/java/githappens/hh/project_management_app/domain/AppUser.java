package githappens.hh.project_management_app.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.persistence.CascadeType;

@Entity(name = "app_user")
public class AppUser {

    // appUserId, userName, firstName, lastName, email,
    // passwordHash, registeredAt, projects, tasks, comments

    // appUserId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_user_id", nullable = false, updatable = false)
    private Long appUserId;

    // username
    @Column(name = "user_name", nullable = false, unique = true, updatable = false) // usernames must be unique and can't
                                                                                    // be changed
    @NotBlank(message = "A unique username is required")
    private String username;

    // firstName
    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "First name required")
    private String firstName;

    // lastName
    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Last name required")
    private String lastName;

    // email
    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "Email already in use")
    private String email;

    // passwordHash
    @Column(name = "password_hash", nullable = false)
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$", message = "Password must contain an uppercase letter, a number, and a special character")
    @NotBlank(message = "Password is required")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String passwordHash;

    // registeredAt
    @Column(name = "registered_at", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // yyyy-MM-dd'T'HH:mm
    private LocalDateTime registeredAt;

    // LISTS OF PROJECTS, TASKS AND COMMENTS

    // projects
    @ManyToMany
    // many-to-many relatonship requires a new join table
    @JoinTable(name = "user_project", joinColumns = @JoinColumn(name = "app_user_id"), inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> projects = new ArrayList<>();

    // tasksAssigned
    @OneToMany(mappedBy = "assignedUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Task> tasksAssigned = new ArrayList<>();

    // tasksCreated
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Task> tasksCreated = new ArrayList<>();

    // comments
    @OneToMany(mappedBy = "commenter", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>(); 

// CONSTRUCTORS

    public AppUser() {
    }

    public AppUser(@NotBlank(message = "A unique username is required") String username,
            @NotBlank(message = "First name required") String firstName,
            @NotBlank(message = "Last name required") String lastName,
            @NotBlank(message = "Email already in use") String email,
            @Size(min = 8, message = "Password must be at least 8 characters") @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$", message = "Password must contain an uppercase letter, a number, and a special character") @NotBlank(message = "Password is required") String passwordHash,
            LocalDateTime registeredAt) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.registeredAt = registeredAt;
    }


// GETTERS AND SETTERS

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Task> getTasksAssigned() {
        return tasksAssigned;
    }

    public void setTasksAssigned(List<Task> tasksAssigned) {
        this.tasksAssigned = tasksAssigned;
    }

    public List<Task> getTasksCreated() {
        return tasksCreated;
    }

    public void setTasksCreated(List<Task> tasksCreated) {
        this.tasksCreated = tasksCreated;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
// TO STRING

    @Override
    public String toString() {
        return "AppUser [username=" + username + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
                + email + ", passwordHash=" + passwordHash + ", registeredAt=" + registeredAt + "]";
    }
    

}
