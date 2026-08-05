package githappens.hh.project_management_app.web;

import githappens.hh.project_management_app.domain.EnumProjectRole;

public record MemberResponse(
    Long appUserId,
    String username,
    EnumProjectRole role
) {}

    
