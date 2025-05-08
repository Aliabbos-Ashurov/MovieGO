package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record TicketCreateDto(
        @NotNull(message = "Event ID is required")
        Long eventId,

        @Positive
        @NotNull Integer rows,

        @Positive
        @NotNull Integer columns,

        @NotNull(message = "User ID is required")
        Long userId
) implements Request {
}
