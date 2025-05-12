package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.TicketCreateDto;
import com.abbos.moviego.dto.TicketResponseDto;
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

    public TicketServiceImpl(TicketRepository repository,
                             TicketMapper ticketMapper,
                             EventService eventService,
                             UserService userService) {
        super(repository, ticketMapper);
        this.eventService = eventService;
        this.userService = userService;
    }

    @Transactional
    @Override
    public TicketResponseDto create(TicketCreateDto dto) {
        Event event = eventService.findEntity(dto.eventId());
        User user = userService.findEntity(dto.userId());

        Ticket ticket = mapper.createFrom(dto, user, event);
        Ticket savedTicket = repository.save(ticket);

        return mapper.toDto(savedTicket);
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

    @Override
    public TicketResponseDto findTicketsByEventId(Long eventId) {
        return mapper.toDto(
                repository.findTicketsByEventId(eventId)
        );
    }

    private void throwNotFound(Long id) {
        throw returnNotFound(id);
    }

    private ResourceNotFoundException returnNotFound(Long id) {
        return new ResourceNotFoundException("Ticket not found with id: " + id);
    }
}
