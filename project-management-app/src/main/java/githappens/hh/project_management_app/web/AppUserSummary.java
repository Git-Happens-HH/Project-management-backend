package githappens.hh.project_management_app.web;

public record AppUserSummary(
    Long appUserId,
    String username,
    String email
) {}