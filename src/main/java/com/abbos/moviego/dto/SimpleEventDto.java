package com.abbos.moviego.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-17
 */
@Getter
@AllArgsConstructor
public class SimpleEventDto {
    private Long id;
    private LocalDateTime showTime;
}
