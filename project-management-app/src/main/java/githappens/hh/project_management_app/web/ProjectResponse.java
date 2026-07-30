package githappens.hh.project_management_app.web;

import java.time.LocalDateTime;
import githappens.hh.project_management_app.domain.EnumProjectRole;

public record ProjectResponse(
    Long projectId,
    String title,
    String description,
    LocalDateTime createdAt,
    EnumProjectRole role
) {}