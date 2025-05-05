package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record CategoryUpdateDto(
        Long id,
        String name,
        String description
) implements Request {
}
