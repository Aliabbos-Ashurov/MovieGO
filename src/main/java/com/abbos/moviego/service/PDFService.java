package com.abbos.moviego.service;

import com.abbos.moviego.dto.internal.TicketRenderDto;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-16
 */
public interface PDFService {
    byte[] generateTicketPdf(TicketRenderDto ticket);
}
