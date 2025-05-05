package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record MovieUpdateDto(
        Long id,
        String title,
        Integer durationMinutes,
        String genre,
        String language,
        String rating,
        String trailerLink,
        String description
) implements Request {
}
