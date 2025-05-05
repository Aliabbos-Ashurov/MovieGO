package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record EventCreateDto(
        @NotNull(message = "Movie ID is required")
        Long movieId,

        @NotNull(message = "Cinema hall ID is required")
        Long cinemaHallId,

        @NotNull(message = "Show time is required")
        LocalDateTime showTime,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price
) implements Request {
}
