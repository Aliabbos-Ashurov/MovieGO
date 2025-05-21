package com.abbos.moviego.dto.internal;

import com.abbos.moviego.dto.base.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-16
 */
public record TicketRenderDto(
        Long id,
        int row,
        int column,
        BigDecimal price,
        String userEmail,
        String movieTitle,
        LocalDateTime showTime,
        String cinemaHallName
) implements DTO {
}
