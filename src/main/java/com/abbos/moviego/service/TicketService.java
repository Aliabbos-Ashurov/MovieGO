package com.abbos.moviego.service;

import com.abbos.moviego.dto.TicketCreateDto;
import com.abbos.moviego.dto.TicketResponseDto;
import com.abbos.moviego.entity.Ticket;
import com.abbos.moviego.service.base.CreateService;
import com.abbos.moviego.service.base.DeleteService;
import com.abbos.moviego.service.base.SearchService;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
public interface TicketService extends CreateService<TicketResponseDto, TicketCreateDto>,
        SearchService<Long, TicketResponseDto>, DeleteService<Long> {

    List<Ticket> findTicketsByUserId(Long userId);

    Ticket findTicketsByEventId(Long eventId);
}
