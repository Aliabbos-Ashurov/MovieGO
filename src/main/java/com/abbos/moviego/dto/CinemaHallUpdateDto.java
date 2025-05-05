package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;
import com.abbos.moviego.enums.CinemaHallStatus;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record CinemaHallUpdateDto(
        Long id,
        CinemaHallStatus status
) implements Request {
}
