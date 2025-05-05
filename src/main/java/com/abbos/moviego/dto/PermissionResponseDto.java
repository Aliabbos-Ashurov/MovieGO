package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Response;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record PermissionResponseDto(
        Long id,
        String name
) implements Response {
}
