package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record PermissionUpdateDto(
        @NotNull
        Long id,

        @NotNull
        @NotBlank(message = "Permission name is required")
        @Size(max = 50, message = "Permission name must not exceed {max} characters")
        String name
) implements Request {
}
