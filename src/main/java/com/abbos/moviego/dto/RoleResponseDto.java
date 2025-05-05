package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Response;

import java.util.Set;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record RoleResponseDto(
        Long id,
        String name,
        Set<PermissionResponseDto> permissions
) implements Response {
}
