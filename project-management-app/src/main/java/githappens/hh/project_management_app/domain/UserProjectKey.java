package githappens.hh.project_management_app.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class UserProjectKey implements Serializable {

// this class is for the join table "user_projects", since user and project have a M:N relationship

    // appUserId
    @Column(name = "app_user_id") 
    Long appUserId; 

    // projectId
    @Column(name = "project_id") 
    Long projectId;

// Constructor

    public UserProjectKey() {
    }

// Getters and setters

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

// Hash code

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((appUserId == null) ? 0 : appUserId.hashCode());
        result = prime * result + ((projectId == null) ? 0 : projectId.hashCode());
        return result;
    }

// Equals

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserProjectKey other = (UserProjectKey) obj;
        if (appUserId == null) {
            if (other.appUserId != null)
                return false;
        } else if (!appUserId.equals(other.appUserId))
            return false;
        if (projectId == null) {
            if (other.projectId != null)
                return false;
        } else if (!projectId.equals(other.projectId))
            return false;
        return true;
    } 
}
