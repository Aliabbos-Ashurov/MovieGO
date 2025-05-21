package com.abbos.moviego.service;

import com.abbos.moviego.dto.internal.WelcomeEmailDto;

/**
 * Service interface for sending emails.
 *
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-20
 */
public interface EmailService {

    /**
     * Sends an email with the specified content.
     *
     * @param dto The dto class for transferring user's email and fullname
     */
    void send(WelcomeEmailDto dto);
}
