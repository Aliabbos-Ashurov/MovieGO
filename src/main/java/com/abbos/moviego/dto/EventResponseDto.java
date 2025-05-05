package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Response;
import com.abbos.moviego.enums.EventStatus;

import java.time.LocalDateTime;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record EventResponseDto(
        Long id,
        MovieResponseDto movie,
        CinemaHallResponseDto cinemaHall,
        LocalDateTime showTime,
        String price,
        EventStatus status,
        Integer capacity,
        ImageResponseDto banner,
        LocalDateTime createdAt
) implements Response {
}
