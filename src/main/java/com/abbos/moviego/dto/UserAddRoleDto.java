package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-12
 */
public record UserAddRoleDto(
        @NotNull
        Long id,

        @NotNull
        Set<Long> roles
) implements Request {
}
