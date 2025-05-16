package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-15
 */
public record SeatInfoDto(
        Long eventId,
        Integer totalRows,
        Integer totalColumns,
        List<SeatDto> takenSeats,
        Integer remainingCapacity
) implements Request {
    public record SeatDto(
            Integer row,
            Integer column
    ) {
    }
}
