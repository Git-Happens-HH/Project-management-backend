package githappens.hh.project_management_app.web;

import githappens.hh.project_management_app.domain.EnumProjectRole;

public record AppUserResponse(
    Long appUserId,
    String username,
    EnumProjectRole role,
) {}

    
