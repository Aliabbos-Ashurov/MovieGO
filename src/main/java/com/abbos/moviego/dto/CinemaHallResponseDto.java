package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Response;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record CinemaHallResponseDto(
        Long id,
        String name,
        Integer capacity,
        String status,
        ImageResponseDto image
) implements Response {
}
