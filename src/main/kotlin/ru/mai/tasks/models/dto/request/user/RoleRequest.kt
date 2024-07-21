package ru.mai.tasks.models.dto.request.user

import com.fasterxml.jackson.annotation.JsonInclude
import ru.mai.tasks.models.entity.user.Role
import jakarta.validation.constraints.NotBlank

data class RoleRequest(
    @field:NotBlank
    @JsonInclude(JsonInclude.Include.USE_DEFAULTS)
    val role: Role
)