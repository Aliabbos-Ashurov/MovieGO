package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;
import com.abbos.moviego.enums.EventStatus;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record EventUpdateDto(
        Long id,
        EventStatus status
) implements Request {
}
