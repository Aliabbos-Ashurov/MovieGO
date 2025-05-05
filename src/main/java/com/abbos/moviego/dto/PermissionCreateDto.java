package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record PermissionCreateDto(
        @NotBlank(message = "Permission name is required")
        @Size(max = 50, message = "Permission name must not exceed 50 characters")
        String name
) implements Request {
}
