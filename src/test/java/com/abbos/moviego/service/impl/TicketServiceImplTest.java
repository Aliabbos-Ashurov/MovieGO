package com.abbos.moviego.service.impl;

import com.abbos.moviego.config.security.SessionUser;
import com.abbos.moviego.dto.*;
import com.abbos.moviego.dto.internal.TicketRenderDto;
import com.abbos.moviego.entity.CinemaHall;
import com.abbos.moviego.entity.Event;
import com.abbos.moviego.entity.Ticket;
import com.abbos.moviego.entity.User;
import com.abbos.moviego.enums.CinemaHallStatus;
import com.abbos.moviego.enums.EventStatus;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.TicketMapper;
import com.abbos.moviego.repository.TicketRepository;
import com.abbos.moviego.service.EventService;
import com.abbos.moviego.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private EventService eventService;

    @Mock
    private UserService userService;

    @Mock
    private SessionUser sessionUser;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private User user;
    private Event event;
    private Ticket ticket;
    private TicketResponseDto ticketResponseDto;
    private TicketCreateDto ticketCreateDto;
    private TicketRenderDto ticketRenderDto;
    private SeatInfoDto seatInfoDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .fullname("John Doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .roles(new HashSet<>())
                .createdAt(LocalDateTime.now())
                .build();

        CinemaHall cinemaHall = CinemaHall.builder()
                .id(1L)
                .name("Hall 1")
                .capacity(100)
                .rows(10)
                .columns(10)
                .status(CinemaHallStatus.ACTIVE)
                .build();

        event = Event.builder()
                .id(1L)
                .cinemaHall(cinemaHall)
                .price(new BigDecimal("10.00"))
                .capacity(50)
                .showTime(LocalDateTime.now())
                .status(EventStatus.SCHEDULED)
                .build();

        ticket = Ticket.builder()
                .id(1L)
                .event(event)
                .rows(1)
                .columns(1)
                .price(new BigDecimal("10.00"))
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        ticketResponseDto = new TicketResponseDto(
                1L,
                new EventResponseDto(1L, null, null, null, "10.00", null, 50, null),
                1,
                1,
                "10.00",
                new UserResponseDto(1L, "John Doe", "john.doe@example.com", null, null, LocalDateTime.now()),
                LocalDateTime.now()
        );

        ticketCreateDto = new TicketCreateDto(
                1L,
                List.of(new TicketCreateDto.SeatPositions(1, 1))
        );

        ticketRenderDto = new TicketRenderDto(1L, 1, 1, new BigDecimal("1"),
                                              "aliabbosashurov@gmail.com", "Some Title", null, "IMAX");

        seatInfoDto = new SeatInfoDto(
                1L,
                10,
                10,
                List.of(new SeatInfoDto.SeatDto(1, 1)),
                50
        );
    }

    @Test
    @DisplayName("Create ticket with valid TicketCreateDto and save successfully")
    void create_shouldSaveTicketWhenValidDtoProvided() {
        when(sessionUser.getId()).thenReturn(1L);
        when(eventService.findEntity(1L)).thenReturn(event);
        when(userService.findEntity(1L)).thenReturn(user);
        when(ticketRepository.existsByEventIdAndSeat(1L, 1, 1)).thenReturn(false);
        when(eventService.decreaseCapacity(1L)).thenReturn(1);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        ticketService.create(ticketCreateDto);

        verify(sessionUser, times(1)).getId();
        verify(eventService, times(1)).findEntity(1L);
        verify(userService, times(1)).findEntity(1L);
        verify(ticketRepository, times(1)).existsByEventIdAndSeat(1L, 1, 1);
        verify(eventService, times(1)).decreaseCapacity(1L);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(ticketMapper, never()).toDto(any(Ticket.class));
    }

    @Test
    @DisplayName("Throw IllegalStateException when requested seats exceed event capacity")
    void create_shouldThrowExceptionWhenSeatsExceedCapacity() {
        TicketCreateDto largeDto = new TicketCreateDto(1L, List.of(
                new TicketCreateDto.SeatPositions(1, 1),
                new TicketCreateDto.SeatPositions(1, 2)
        ));
        when(eventService.findEntity(1L)).thenReturn(Event.builder().capacity(1).build());
        when(sessionUser.getId()).thenReturn(1L);
        when(userService.findEntity(1L)).thenReturn(user);

        assertThrows(IllegalStateException.class, () -> ticketService.create(largeDto));

        verify(eventService, times(1)).findEntity(1L);
        verify(sessionUser, times(1)).getId();
        verify(userService, times(1)).findEntity(1L);
        verify(ticketRepository, never()).existsByEventIdAndSeat(anyLong(), anyInt(), anyInt());
        verify(eventService, never()).decreaseCapacity(anyLong());
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    @DisplayName("Throw IllegalArgumentException when seat is out of bounds")
    void create_shouldThrowExceptionWhenSeatOutOfBounds() {
        TicketCreateDto invalidDto = new TicketCreateDto(1L, List.of(new TicketCreateDto.SeatPositions(11, 1)));
        when(sessionUser.getId()).thenReturn(1L);
        when(eventService.findEntity(1L)).thenReturn(event);
        when(userService.findEntity(1L)).thenReturn(user);

        assertThrows(IllegalArgumentException.class, () -> ticketService.create(invalidDto));

        verify(sessionUser, times(1)).getId();
        verify(eventService, times(1)).findEntity(1L);
        verify(userService, times(1)).findEntity(1L);
        verify(ticketRepository, never()).existsByEventIdAndSeat(anyLong(), anyInt(), anyInt());
        verify(eventService, never()).decreaseCapacity(anyLong());
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    @DisplayName("Throw IllegalStateException when seat is already taken")
    void create_shouldThrowExceptionWhenSeatAlreadyTaken() {
        when(sessionUser.getId()).thenReturn(1L);
        when(eventService.findEntity(1L)).thenReturn(event);
        when(userService.findEntity(1L)).thenReturn(user);
        when(ticketRepository.existsByEventIdAndSeat(1L, 1, 1)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> ticketService.create(ticketCreateDto));

        verify(sessionUser, times(1)).getId();
        verify(eventService, times(1)).findEntity(1L);
        verify(userService, times(1)).findEntity(1L);
        verify(ticketRepository, times(1)).existsByEventIdAndSeat(1L, 1, 1);
        verify(eventService, never()).decreaseCapacity(anyLong());
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    @DisplayName("Throw IllegalStateException when no tickets are available")
    void create_shouldThrowExceptionWhenNoTicketsAvailable() {
        when(sessionUser.getId()).thenReturn(1L);
        when(eventService.findEntity(1L)).thenReturn(event);
        when(userService.findEntity(1L)).thenReturn(user);
        when(ticketRepository.existsByEventIdAndSeat(1L, 1, 1)).thenReturn(false);
        when(eventService.decreaseCapacity(1L)).thenReturn(0);

        assertThrows(IllegalStateException.class, () -> ticketService.create(ticketCreateDto));

        verify(sessionUser, times(1)).getId();
        verify(eventService, times(1)).findEntity(1L);
        verify(userService, times(1)).findEntity(1L);
        verify(ticketRepository, times(1)).existsByEventIdAndSeat(1L, 1, 1);
        verify(eventService, times(1)).decreaseCapacity(1L);
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    @DisplayName("Find ticket for rendering by ID")
    void findTicketForRender_shouldReturnTicketRenderDtoWhenExists() {
        when(ticketRepository.findTicketForRender(1L)).thenReturn(ticketRenderDto);

        TicketRenderDto result = ticketService.findTicketForRender(1L);

        verify(ticketRepository, times(1)).findTicketForRender(1L);
        assertEquals(ticketRenderDto, result);
    }

    @Test
    @DisplayName("Return seat information for an event")
    void getSeatInfo_shouldReturnSeatInfoDto() {
        when(eventService.findEntity(1L)).thenReturn(event);
        when(ticketRepository.findByEventId(1L)).thenReturn(List.of(ticket));

        SeatInfoDto result = ticketService.getSeatInfo(1L);

        verify(eventService, times(1)).findEntity(1L);
        verify(ticketRepository, times(1)).findByEventId(1L);
        assertEquals(seatInfoDto.eventId(), result.eventId());

        assertEquals(seatInfoDto.takenSeats().size(), result.takenSeats().size());
    }

    @Test
    @DisplayName("Return authenticated user's tickets")
    void getMyTickets_shouldReturnUserTickets() {
        when(sessionUser.getId()).thenReturn(1L);
        when(ticketRepository.findTicketsByUserId(1L)).thenReturn(List.of(ticket));
        when(ticketMapper.toDtoList(List.of(ticket))).thenReturn(List.of(ticketResponseDto));

        List<TicketResponseDto> result = ticketService.getMyTickets();

        verify(sessionUser, times(1)).getId();
        verify(ticketRepository, times(1)).findTicketsByUserId(1L);
        verify(ticketMapper, times(1)).toDtoList(List.of(ticket));
        assertEquals(List.of(ticketResponseDto), result);
    }

    @Test
    @DisplayName("Delete ticket when ticket exists")
    void delete_shouldDeleteTicketWhenExists() {
        when(ticketRepository.existsById(1L)).thenReturn(true);

        ticketService.delete(1L);

        verify(ticketRepository, times(1)).existsById(1L);
        verify(ticketRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when deleting non-existent ticket")
    void delete_shouldThrowNotFoundWhenTicketDoesNotExist() {
        when(ticketRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> ticketService.delete(1L));

        verify(ticketRepository, times(1)).existsById(1L);
        verify(ticketRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Return true when ticket exists by ID")
    void exists_shouldReturnTrueWhenTicketExists() {
        when(ticketRepository.existsById(1L)).thenReturn(true);

        boolean result = ticketService.exists(1L);

        verify(ticketRepository, times(1)).existsById(1L);
        assertTrue(result);
    }

    @Test
    @DisplayName("Return false when ticket does not exist by ID")
    void exists_shouldReturnFalseWhenTicketDoesNotExist() {
        when(ticketRepository.existsById(1L)).thenReturn(false);

        boolean result = ticketService.exists(1L);

        verify(ticketRepository, times(1)).existsById(1L);
        assertFalse(result);
    }

    @Test
    @DisplayName("Find and return TicketResponseDto for existing ticket")
    void find_shouldReturnTicketDtoWhenTicketExists() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketMapper.toDto(ticket)).thenReturn(ticketResponseDto);

        TicketResponseDto result = ticketService.find(1L);

        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketMapper, times(1)).toDto(ticket);
        assertEquals(ticketResponseDto, result);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when finding non-existent ticket")
    void find_shouldThrowNotFoundWhenTicketDoesNotExist() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ticketService.find(1L));

        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketMapper, never()).toDto(any(Ticket.class));
    }

    @Test
    @DisplayName("Find and return Ticket entity for existing ticket")
    void findEntity_shouldReturnTicketWhenExists() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Ticket result = ticketService.findEntity(1L);

        verify(ticketRepository, times(1)).findById(1L);
        assertEquals(ticket, result);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when finding non-existent ticket entity")
    void findEntity_shouldThrowNotFoundWhenTicketDoesNotExist() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ticketService.findEntity(1L));

        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Return true when seat is taken for event")
    void existsByEventIdAndSeat_shouldReturnTrueWhenSeatTaken() {
        when(ticketRepository.existsByEventIdAndSeat(1L, 1, 1)).thenReturn(true);

        boolean result = ticketService.existsByEventIdAndSeat(1L, 1, 1);

        verify(ticketRepository, times(1)).existsByEventIdAndSeat(1L, 1, 1);
        assertTrue(result);
    }

    @Test
    @DisplayName("Return false when seat is not taken for event")
    void existsByEventIdAndSeat_shouldReturnFalseWhenSeatNotTaken() {
        when(ticketRepository.existsByEventIdAndSeat(1L, 1, 1)).thenReturn(false);

        boolean result = ticketService.existsByEventIdAndSeat(1L, 1, 1);

        verify(ticketRepository, times(1)).existsByEventIdAndSeat(1L, 1, 1);
        assertFalse(result);
    }

    @Test
    @DisplayName("Find and return tickets by event ID")
    void findByEventId_shouldReturnTicketsForEvent() {
        when(ticketRepository.findByEventId(1L)).thenReturn(List.of(ticket));

        List<Ticket> result = ticketService.findByEventId(1L);

        verify(ticketRepository, times(1)).findByEventId(1L);
        assertEquals(List.of(ticket), result);
    }

    @Test
    @DisplayName("Find and return tickets by user ID")
    void findTicketsByUserId_shouldReturnUserTickets() {
        when(ticketRepository.findTicketsByUserId(1L)).thenReturn(List.of(ticket));
        when(ticketMapper.toDtoList(List.of(ticket))).thenReturn(List.of(ticketResponseDto));

        List<TicketResponseDto> result = ticketService.findTicketsByUserId(1L);

        verify(ticketRepository, times(1)).findTicketsByUserId(1L);
        verify(ticketMapper, times(1)).toDtoList(List.of(ticket));
        assertEquals(List.of(ticketResponseDto), result);
    }

    @Test
    @DisplayName("Return list of all TicketResponseDto")
    void findAll_shouldReturnListOfTicketDtos() {
        when(ticketRepository.findAll()).thenReturn(List.of(ticket));
        when(ticketMapper.toDtoList(List.of(ticket))).thenReturn(List.of(ticketResponseDto));

        List<TicketResponseDto> result = ticketService.findAll();

        verify(ticketRepository, times(1)).findAll();
        verify(ticketMapper, times(1)).toDtoList(List.of(ticket));
        assertEquals(List.of(ticketResponseDto), result);
    }
}