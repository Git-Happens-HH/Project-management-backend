package githappens.hh.project_management_app.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;

// Resolves the currently authenticated AppUser from the security context
//Shared by controllers that need to know "who is making this request"
 // (e.g. to assign project ownership, or to check membership/role)
@Service
public class CurrentUserService {

    private final AppUserRepository appUserRepository;

    public CurrentUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    // returns the full AppUser for the authenticated requester
    public AppUser getRequester() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    // convenience method when only the id is needed
    public Long getRequesterUserId() {
        return getRequester().getAppUserId();
    }
}