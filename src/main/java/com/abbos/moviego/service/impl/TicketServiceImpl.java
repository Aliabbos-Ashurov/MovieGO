package com.abbos.moviego.service.impl;

import com.abbos.moviego.config.security.SessionUser;
import com.abbos.moviego.dto.SeatInfoDto;
import com.abbos.moviego.dto.TicketCreateDto;
import com.abbos.moviego.dto.TicketResponseDto;
import com.abbos.moviego.dto.render.TicketRenderDto;
import com.abbos.moviego.entity.CinemaHall;
import com.abbos.moviego.entity.Event;
import com.abbos.moviego.entity.Ticket;
import com.abbos.moviego.entity.User;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.TicketMapper;
import com.abbos.moviego.repository.TicketRepository;
import com.abbos.moviego.service.EventService;
import com.abbos.moviego.service.TicketService;
import com.abbos.moviego.service.UserService;
import com.abbos.moviego.service.base.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of TicketService handling ticket-related operations.
 *
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-07
 */
@Service
public class TicketServiceImpl extends AbstractService<TicketRepository, TicketMapper> implements TicketService {

    private final EventService eventService;
    private final UserService userService;
    private final SessionUser sessionUser;

    public TicketServiceImpl(TicketRepository repository,
                             TicketMapper ticketMapper,
                             EventService eventService,
                             UserService userService,
                             SessionUser sessionUser) {
        super(repository, ticketMapper);
        this.eventService = eventService;
        this.userService = userService;
        this.sessionUser = sessionUser;
    }


    @Transactional
    @Override
    public void create(TicketCreateDto dto) {
        Event event = eventService.findEntity(dto.eventId());
        User user = userService.findEntity(sessionUser.getId());
        CinemaHall cinemaHall = event.getCinemaHall();

        if (dto.positions().size() > event.getCapacity()) {
            throw new IllegalStateException("Requested seats exceed event capacity");
        }

        dto.positions().forEach(pos -> processTicket(pos, cinemaHall, event, user));
    }

    private void processTicket(TicketCreateDto.SeatPositions seatPositions,
                               CinemaHall cinemaHall,
                               Event event,
                               User user) {
        Integer row = seatPositions.row();
        Integer column = seatPositions.column();

        if (row > cinemaHall.getRows() || column > cinemaHall.getColumns()
                || row < 1 || column < 1) {
            throw new IllegalArgumentException("Seat (" + row + ", " + column + ") is out of bounds for cinema hall");
        }

        if (existsByEventIdAndSeat(event.getId(), row, column)) {
            throw new IllegalStateException("Seat (" + row + ", " + column + ") is already taken");
        }

        int affectedRows = eventService.decreaseCapacity(event.getId());
        if (affectedRows == 0) {
            throw new IllegalStateException("No tickets available for event ID: " + event.getId());
        }

        Ticket ticket = Ticket.builder()
                .event(event)
                .rows(row)
                .columns(column)
                .price(event.getPrice())
                .user(user)
                .build();

        repository.save(ticket);
    }

    @Override
    public TicketRenderDto findTicketForRender(Long ticketId) {
        return repository.findTicketForRender(ticketId);
    }

    @Transactional(readOnly = true)
    @Override
    public SeatInfoDto getSeatInfo(Long eventId) {
        Event event = eventService.findEntity(eventId);

        CinemaHall cinemaHall = event.getCinemaHall();

        List<Ticket> tickets = findByEventId(eventId);
        List<SeatInfoDto.SeatDto> takenSeats = tickets.stream()
                .map(ticket -> new SeatInfoDto.SeatDto(ticket.getRows(), ticket.getColumns()))
                .toList();

        return new SeatInfoDto(
                eventId,
                cinemaHall.getRows(),
                cinemaHall.getColumns(),
                takenSeats,
                event.getCapacity()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponseDto> getMyTickets() {
        return findTicketsByUserId(sessionUser.getId());
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!exists(id)) {
            throwNotFound(id);
        }
        repository.deleteById(id);
    }

    @Override
    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public TicketResponseDto find(Long id) {
        return mapper.toDto(findEntity(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Ticket findEntity(Long id) {
        return repository.findById(id).orElseThrow(
                () -> returnNotFound(id)
        );
    }

    @Override
    public boolean existsByEventIdAndSeat(Long eventId, Integer row, Integer column) {
        return repository.existsByEventIdAndSeat(eventId, row, column);
    }

    @Override
    public List<Ticket> findByEventId(Long eventId) {
        return repository.findByEventId(eventId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TicketResponseDto> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public List<TicketResponseDto> findTicketsByUserId(Long userId) {
        return mapper.toDtoList(
                repository.findTicketsByUserId(userId)
        );
    }

    private void throwNotFound(Long id) {
        throw returnNotFound(id);
    }

    private ResourceNotFoundException returnNotFound(Long id) {
        return new ResourceNotFoundException("Ticket not found with id: " + id);
    }
}
