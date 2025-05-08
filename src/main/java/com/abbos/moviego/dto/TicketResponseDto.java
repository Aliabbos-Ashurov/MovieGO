package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Response;

import java.time.LocalDateTime;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record TicketResponseDto(
        Long id,
        EventResponseDto event,
        Integer rows,
        Integer columns,
        String price,
        UserResponseDto user,
        LocalDateTime createdAt
) implements Response {
}
