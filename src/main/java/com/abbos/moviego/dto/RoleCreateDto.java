package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record RoleCreateDto(
        @NotBlank(message = "Role name is required")
        @Size(max = 30, message = "Role name must not exceed 30 characters")
        String name,
        Set<Long> permissions
) implements Request {
}
