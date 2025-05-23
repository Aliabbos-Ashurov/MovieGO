package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.*;
import com.abbos.moviego.entity.CinemaHall;
import com.abbos.moviego.entity.Event;
import com.abbos.moviego.entity.Movie;
import com.abbos.moviego.enums.EventStatus;
import com.abbos.moviego.mapper.base.UtilMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        EventMapperImpl.class, MovieMapperImpl.class, CinemaHallMapperImpl.class,
        ImageMapperImpl.class, UtilMapperImpl.class, CategoryMapperImpl.class, SceneImageMapperImpl.class })
class EventMapperTest {

    @Autowired
    private EventMapper eventMapper;

    @Test
    void testToDto() {
        Event entity = EventBuilder.defaultEvent();

        EventResponseDto dto = eventMapper.toDto(entity);

        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getMovie().getId(), dto.movie().id());
        assertEquals(entity.getCinemaHall().getId(), dto.cinemaHall().id());
        assertEquals(entity.getShowTime(), dto.showTime());
        assertEquals(entity.getPrice().toString(), dto.price());
        assertEquals(entity.getStatus(), dto.status());
        assertEquals(entity.getCapacity(), dto.capacity());
        assertEquals(entity.getCreatedAt(), dto.createdAt());
        assertNull(eventMapper.toDto(null));
    }

    @Test
    void testFromCreate() {
        EventCreateDto dto = DtoBuilder.defaultCreateDto();

        Event entity = eventMapper.fromCreate(dto);

        assertNull(entity.getId());
        assertEquals(dto.showTime(), entity.getShowTime());
        assertEquals(dto.price(), entity.getPrice());
        assertEquals(EventStatus.SCHEDULED, entity.getStatus());
        assertNull(entity.getMovie());
        assertNull(entity.getCinemaHall());
        assertNull(eventMapper.fromCreate(null));
    }

    @Test
    void testFromUpdate() {
        EventUpdateDto dto = DtoBuilder.defaultUpdateDto();

        Event entity = eventMapper.fromUpdate(dto);

        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.status(), entity.getStatus());
        assertNull(eventMapper.fromUpdate(null));
    }

    @Test
    void testMergeUpdate() {
        EventUpdateDto dto = DtoBuilder.defaultUpdateDto();
        Event target = EventBuilder.defaultEvent();

        Event merged = eventMapper.mergeUpdate(target, dto);

        assertEquals(target.getId(), merged.getId());
        assertEquals(dto.status(), merged.getStatus());
        assertEquals(target.getMovie(), merged.getMovie());
        assertEquals(target.getCinemaHall(), merged.getCinemaHall());
        assertEquals(target.getShowTime(), merged.getShowTime());
        assertEquals(target.getPrice(), merged.getPrice());
        assertEquals(target.getCapacity(), merged.getCapacity());
        assertEquals(target, eventMapper.mergeUpdate(target, null));
    }

    @Test
    void testToDtoList() {
        List<Event> list = List.of(EventBuilder.defaultEvent());

        List<EventResponseDto> dtos = eventMapper.toDtoList(list);

        assertEquals(1, dtos.size());
        assertEquals(list.getFirst().getId(), dtos.getFirst().id());
        assertNull(eventMapper.toDtoList(null));
    }

    @Test
    void testToEntityList() {
        List<EventResponseDto> dtos = List.of(DtoBuilder.defaultResponseDto());

        List<Event> entities = eventMapper.toEntityList(dtos);

        assertEquals(1, entities.size());
        assertEquals(dtos.getFirst().id(), entities.getFirst().getId());
        assertNull(eventMapper.toEntityList(null));
    }

    static class EventBuilder {
        static Event defaultEvent() {
            Movie movie = Movie.builder()
                    .id(1L)
                    .title("Test Movie")
                    .build();

            CinemaHall cinemaHall = CinemaHall.builder()
                    .id(1L)
                    .name("Hall A")
                    .build();

            return Event.builder()
                    .id(1L)
                    .movie(movie)
                    .cinemaHall(cinemaHall)
                    .showTime(LocalDateTime.of(2025, 5, 24, 18, 0))
                    .price(new BigDecimal("15.00"))
                    .status(EventStatus.SCHEDULED)
                    .capacity(100)
                    .createdAt(LocalDateTime.now())
                    .build();
        }
    }

    static class DtoBuilder {
        static EventCreateDto defaultCreateDto() {
            return new EventCreateDto(
                    1L,
                    1L,
                    LocalDateTime.of(2025, 5, 24, 18, 0),
                    new BigDecimal("15.00")
            );
        }

        static EventUpdateDto defaultUpdateDto() {
            return new EventUpdateDto(1L, EventStatus.CANCELLED);
        }

        static EventResponseDto defaultResponseDto() {
            var movieDto = new MovieResponseDto(1L, "Test Movie", 1, null, null, null, null, null, null, null, null);
            var cinemaHallDto = new CinemaHallResponseDto(1L, "Hall A", 100, 10, 10, "ACTIVE", null);
            return new EventResponseDto(
                    1L,
                    movieDto,
                    cinemaHallDto,
                    LocalDateTime.of(2025, 5, 24, 18, 0),
                    "15.00",
                    EventStatus.SCHEDULED,
                    100,
                    LocalDateTime.now()
            );
        }
    }
}
