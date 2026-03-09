package githappens.hh.project_management_app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity(name="user_project")
public class UserProject {

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

    // shared to this user (true/false)
    // if shared = false, this user is the owner of the project
    @Column(name = "shared_to_this_user", updatable = false)
    boolean sharedToThisUser;

// constructors

    public UserProject() {
    }

    public UserProject(AppUser appUser, Project project, boolean sharedToThisUser) {
        this.appUser = appUser;
        this.project = project;
        this.sharedToThisUser = sharedToThisUser;
        this.userProjectKeyId = new UserProjectKey(appUser.getAppUserId(), project.getProjectId());
    }

// getters and setters

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

    public boolean getSharedToThisUser() {
        return sharedToThisUser;
    }

    public void setSharedToThisUser(boolean shared_to_this_user) {
        this.sharedToThisUser = sharedToThisUser;
    }


}
