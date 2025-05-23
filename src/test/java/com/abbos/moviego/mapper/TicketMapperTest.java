package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.*;
import com.abbos.moviego.entity.*;
import com.abbos.moviego.enums.EventStatus;
import com.abbos.moviego.mapper.base.UtilMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        TicketMapperImpl.class,
        EventMapperImpl.class,
        UserMapperImpl.class,
        UtilMapperImpl.class,
        MovieMapperImpl.class,
        CinemaHallMapperImpl.class,
        ImageMapperImpl.class,
        CategoryMapperImpl.class,
        SceneImageMapperImpl.class,
        RoleMapperImpl.class,
        PermissionMapperImpl.class
})
class TicketMapperTest {

    @Autowired
    private TicketMapper ticketMapper;

    @Test
    void testToDto() {
        Ticket entity = TicketBuilder.defaultTicket();

        TicketResponseDto dto = ticketMapper.toDto(entity);

        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getEvent().getId(), dto.event().id());
        assertEquals(entity.getRows(), dto.rows());
        assertEquals(entity.getColumns(), dto.columns());
        assertEquals(entity.getPrice().toString(), dto.price());
        assertEquals(entity.getUser().getId(), dto.user().id());
        assertEquals(entity.getUser().getFullname(), dto.user().fullname());
        assertEquals(entity.getCreatedAt(), dto.createdAt());
        assertNull(ticketMapper.toDto(null));
    }

    @Test
    void testToEntity() {
        TicketResponseDto dto = DtoBuilder.defaultTicketResponseDto();

        Ticket entity = ticketMapper.toEntity(dto);

        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.event().id(), entity.getEvent().getId());
        assertEquals(dto.rows(), entity.getRows());
        assertEquals(dto.columns(), entity.getColumns());
        assertEquals(new BigDecimal(dto.price()), entity.getPrice());
        assertEquals(dto.user().id(), entity.getUser().getId());
        assertEquals(dto.user().fullname(), entity.getUser().getFullname());
        assertNotNull(entity.getCreatedAt());
        assertNull(ticketMapper.toEntity(null));
    }

    @Test
    void testCreateFrom() {
        TicketCreateDto dto = DtoBuilder.defaultCreateDto();
        User user = UserBuilder.defaultUser();
        Event event = EventBuilder.defaultEvent();

        Ticket entity = ticketMapper.createFrom(dto, user, event);

        assertNull(entity.getId());
        assertEquals(event, entity.getEvent());
        assertEquals(event.getPrice(), entity.getPrice());
        assertEquals(user, entity.getUser());
        assertNull(ticketMapper.createFrom(null, null, null));
    }

    @Test
    void testToDtoList() {
        List<Ticket> entities = List.of(TicketBuilder.defaultTicket());

        List<TicketResponseDto> dtos = ticketMapper.toDtoList(entities);

        assertEquals(1, dtos.size());
        TicketResponseDto dto = dtos.getFirst();
        Ticket entity = entities.getFirst();
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getEvent().getId(), dto.event().id());
        assertEquals(entity.getRows(), dto.rows());
        assertEquals(entity.getColumns(), dto.columns());
        assertEquals(entity.getPrice().toString(), dto.price());
        assertEquals(entity.getUser().getId(), dto.user().id());
        assertEquals(entity.getUser().getFullname(), dto.user().fullname());
        assertEquals(entity.getCreatedAt(), dto.createdAt());
        assertNull(ticketMapper.toDtoList(null));
    }

    @Test
    void testToEntityList() {
        List<TicketResponseDto> dtos = List.of(DtoBuilder.defaultTicketResponseDto());

        List<Ticket> entities = ticketMapper.toEntityList(dtos);

        assertEquals(1, entities.size());
        Ticket entity = entities.getFirst();
        TicketResponseDto dto = dtos.getFirst();
        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.event().id(), entity.getEvent().getId());
        assertEquals(dto.rows(), entity.getRows());
        assertEquals(dto.columns(), entity.getColumns());
        assertEquals(new BigDecimal(dto.price()), entity.getPrice());
        assertEquals(dto.user().id(), entity.getUser().getId());
        assertEquals(dto.user().fullname(), entity.getUser().getFullname());
        assertNotNull(entity.getCreatedAt());
        assertNull(ticketMapper.toEntityList(null));
    }

    static class TicketBuilder {
        static Ticket defaultTicket() {
            Event event = EventBuilder.defaultEvent();
            User user = UserBuilder.defaultUser();

            Ticket ticket = Ticket.builder()
                    .id(1L)
                    .event(event)
                    .rows(5)
                    .columns(10)
                    .price(new BigDecimal("15.00"))
                    .user(user)
                    .build();
            ticket.setCreatedAt(LocalDateTime.now());
            return ticket;
        }
    }

    static class EventBuilder {
        static Event defaultEvent() {
            Movie movie = Movie.builder()
                    .id(1L)
                    .title("Test Movie")
                    .build();
            movie.setCreatedAt(LocalDateTime.now());

            CinemaHall cinemaHall = CinemaHall.builder()
                    .id(1L)
                    .name("Hall A")
                    .build();
            cinemaHall.setCreatedAt(LocalDateTime.now());

            Event event = Event.builder()
                    .id(1L)
                    .movie(movie)
                    .cinemaHall(cinemaHall)
                    .showTime(LocalDateTime.of(2025, 5, 24, 18, 0))
                    .price(new BigDecimal("15.00"))
                    .status(EventStatus.SCHEDULED)
                    .capacity(100)
                    .build();
            event.setCreatedAt(LocalDateTime.now());
            return event;
        }
    }

    static class UserBuilder {
        static User defaultUser() {
            Role role = Role.builder()
                    .id(1L)
                    .name("USER")
                    .build();
            role.setCreatedAt(LocalDateTime.now());

            User user = User.builder()
                    .id(1L)
                    .fullname("Test User")
                    .email("test@example.com")
                    .password("password123")
                    .roles(new HashSet<>(Set.of(role)))
                    .build();
            user.setCreatedAt(LocalDateTime.now());
            return user;
        }
    }

    static class DtoBuilder {
        static TicketCreateDto defaultCreateDto() {
            return new TicketCreateDto(
                    1L,
                    List.of(new TicketCreateDto.SeatPositions(5, 10))
            );
        }

        static TicketResponseDto defaultTicketResponseDto() {
            EventResponseDto eventDto = new EventResponseDto(
                    1L,
                    new MovieResponseDto(1L, "Test Movie", null, null, null, null, null, null, null, null, null),
                    new CinemaHallResponseDto(1L, "Hall A", 100, 10, 10, "ACTIVE", null),
                    LocalDateTime.of(2025, 5, 24, 18, 0),
                    "15.00",
                    EventStatus.SCHEDULED,
                    100,
                    LocalDateTime.now()
            );
            UserResponseDto userDto = new UserResponseDto(
                    1L,
                    "Test User",
                    "test@example.com",
                    null,
                    List.of(new RoleResponseDto(1L, "USER", Set.of())),
                    LocalDateTime.now()
            );
            return new TicketResponseDto(
                    1L,
                    eventDto,
                    5,
                    10,
                    "15.00",
                    userDto,
                    LocalDateTime.now()
            );
        }
    }
}