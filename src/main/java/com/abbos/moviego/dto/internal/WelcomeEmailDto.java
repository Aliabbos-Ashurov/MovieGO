package com.abbos.moviego.dto.internal;

import com.abbos.moviego.dto.base.DTO;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-20
 */
public record WelcomeEmailDto(
        String email,
        String fullname
) implements DTO {
}
