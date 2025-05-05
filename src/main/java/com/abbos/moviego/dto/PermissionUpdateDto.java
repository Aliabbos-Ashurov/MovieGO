package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record PermissionUpdateDto(
        Long id,
        String name
) implements Request {
}
