package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.Set;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record TicketCreateDto(
        @NotNull(message = "Event ID is required")
        Long eventId,

        List<SeatPositions> positions
) implements Request {

        public record SeatPositions(
                @Positive
                @NotNull Integer row,

                @Positive
                @NotNull Integer column
        ) {
        }
}
