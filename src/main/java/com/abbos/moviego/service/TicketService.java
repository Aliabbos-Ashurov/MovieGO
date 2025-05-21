package com.abbos.moviego.service;

import com.abbos.moviego.dto.SeatInfoDto;
import com.abbos.moviego.dto.TicketCreateDto;
import com.abbos.moviego.dto.TicketResponseDto;
import com.abbos.moviego.dto.internal.TicketRenderDto;
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
        SearchService<Long, Ticket, TicketResponseDto>, DeleteService<Long> {

    List<TicketResponseDto> findTicketsByUserId(Long userId);

    boolean existsByEventIdAndSeat(Long eventId, Integer row, Integer column);

    List<Ticket> findByEventId(Long eventId);

    SeatInfoDto getSeatInfo(Long eventId);

    List<TicketResponseDto> getMyTickets();

    TicketRenderDto findTicketForRender(Long ticketId);
}
