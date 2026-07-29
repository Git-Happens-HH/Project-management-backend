package githappens.hh.project_management_app.domain;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name="user_project")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"app_user_id", "project_id"}))

public class UserProject {

    // id, userProject, appUser, project, role, joinedAt

    // userProject
    // @EmbeddedId
    // UserProjectKey userProjectKeyId;

    // id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProjectId;
    
    // appUser
    @ManyToOne
    @JoinColumn(name="app_user_id", nullable = false)
    private AppUser appUser;

    // project
    @ManyToOne
    @JoinColumn(name="project_id", nullable = false)
    private Project project;

    // role
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, updatable = true)
    private EnumProjectRole role;

    @Column(name = "joined_at", nullable = false, updatable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // yyyy-MM-dd'T'HH:mm
    private LocalDateTime joinedAt;

// CONSTRUCTORS

    public UserProject() {
    }

    public UserProject(AppUser appUser, Project project, EnumProjectRole role, LocalDateTime joinedAt) {
        this.appUser = appUser;
        this.project = project;
        this.role = role;
        this.joinedAt = joinedAt;
        // this.userProjectKeyId = new UserProjectKey(appUser.getAppUserId(), project.getProjectId());
    }

// GETTERS AND SETTERS

    // public UserProjectKey getUserProjectKeyId() {
    //     return userProjectKeyId;
    // }

    // public void setUserProjectKeyId(UserProjectKey userProjectKeyId) {
    //     this.userProjectKeyId = userProjectKeyId;
    // }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

     public EnumProjectRole getRole() {
        return role;
    }

    public void setRole(EnumProjectRole role) {
        this.role = role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

// TO STRING

    @Override
    public String toString() {
        return "UserProject [appUser=" + appUser + ", project=" + project
                + ", role=" + role + ", joinedAt=" + joinedAt + "]";
    }

    

}
