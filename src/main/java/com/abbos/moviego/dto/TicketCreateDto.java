package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record TicketCreateDto(
        @NotNull(message = "Event ID is required")
        Long eventId,

        @NotBlank(message = "Seat number is required")
        String seatNumber,

        @NotNull(message = "User ID is required")
        Long userId
) implements Request {
}
