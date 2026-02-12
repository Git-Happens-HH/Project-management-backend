package githappens.hh.project_management_app.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name= "AppUsers")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_user_id", nullable = false, updatable = false)
    private Long appUserId;

    @NotBlank(message = "A unique username is required")
    @Column(name = "username", nullable = false, unique = true, updatable = false) // usernames must be unique and cant be changed
    private String userName;

    @NotBlank(message = "First name required")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name required")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Email already in use")
    @Column(name = "email", nullable = false, unique = true) // emails must be unique
    private String email;


    private String passwordHash;
    private LocalDateTime registeredAt;

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
