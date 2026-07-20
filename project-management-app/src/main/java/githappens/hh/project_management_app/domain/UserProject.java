package githappens.hh.project_management_app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity(name="user_project")
public class UserProject {

    // userProject, appUser, project, role

    // userProject
    @EmbeddedId
    UserProjectKey userProjectKeyId;

    // appUser
    @ManyToOne
    @MapsId("appUserId")
    @JoinColumn(name="app_user_id")
    AppUser appUser;

    // project
    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name="project_id")
    Project project;

    // role
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, updatable = true)
    private EnumProjectRole role;

// CONSTRUCTORS

    public UserProject() {
    }

    public UserProject(AppUser appUser, Project project, EnumProjectRole role) {
        this.appUser = appUser;
        this.project = project;
        this.role = role;
        this.userProjectKeyId = new UserProjectKey(appUser.getAppUserId(), project.getProjectId());
    }

// GETTERS AND SETTERS

    public UserProjectKey getUserProjectKeyId() {
        return userProjectKeyId;
    }

    public void setUserProjectKeyId(UserProjectKey userProjectKeyId) {
        this.userProjectKeyId = userProjectKeyId;
    }

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

// TO STRING

    @Override
    public String toString() {
        return "UserProject [appUser=" + appUser + ", project=" + project
                + ", role=" + role + "]";
    }

    

}
