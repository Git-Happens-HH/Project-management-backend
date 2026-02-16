package githappens.hh.project_management_app.domain;

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

// constructors

        public UserProject() {
    }
    




}
