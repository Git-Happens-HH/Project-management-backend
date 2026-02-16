package githappens.hh.project_management_app.domain;

import java.time.LocalDateTime;
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
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Entity(name= "app_user")
public class AppUser {

    // appUserId, userName, firstName, lastName, email, 
    // passwordHash, registeredAt, projects, tasks, comments

    // appUserId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_user_id", nullable = false, updatable = false)
    private Long appUserId;

    // userName
    @Column(name = "user_name", nullable = false, unique = true, updatable = false) // usernames must be unique and cant be changed
    @NotBlank(message = "A unique username is required")
    private String userName;

    // firstName
    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "First name required")
    private String firstName;

    // lastName
    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Last name required")
    private String lastName;

    // email
    @Column(name = "email", nullable = false, unique = true) // emails must be unique
    @NotBlank(message = "Email already in use")
    private String email;

    // passwordHash
    @Column(name = "password_hash", nullable = false)
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
    message = "Password must contain an uppercase letter, a number, and a special character")
    @NotBlank(message = "Password is required")
    private String passwordHash;

    // registeredAt
    @Column(name = "registered_at", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // yyyy-MM-dd'T'HH:mm
    private LocalDateTime registeredAt;

    // LISTS OF PROJECTS, TASKS AND COMMENTS 

    // projects

    @ManyToMany
    // many-to-many relatonship requires a new join table
    @JoinTable(
    name = "user_projects",
    joinColumns = @JoinColumn(name = "app_user_id"),
    inverseJoinColumns = @JoinColumn(name = "project_id")
)

    private List<Project> projects;

    // tasks
    private List<Task> tasks;

    // comments 
    private List<Comment> comments;


    public AppUser() {
    }

    public AppUser(String userName, String firstName, String lastName, String email, String passwordHash,
            LocalDateTime registeredAt) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.registeredAt = registeredAt;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long userId) {
        this.appUserId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    @Override
    public String toString() {
        return "User [userName=" + userName + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
                + ", passwordHash=" + passwordHash + ", registeredAt=" + registeredAt + "]";
    }

}
