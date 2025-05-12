package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-11
 */
public record RoleUpdateDto(
        @NotNull
        Long id,

        @NotBlank(message = "Role name is required")
        @Size(max = 30, message = "Role name must not exceed {max} characters")
        String name
) implements Request {
}
