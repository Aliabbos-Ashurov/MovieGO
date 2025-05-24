package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.*;
import com.abbos.moviego.entity.CinemaHall;
import com.abbos.moviego.entity.Event;
import com.abbos.moviego.entity.Movie;
import com.abbos.moviego.enums.EventStatus;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.EventMapper;
import com.abbos.moviego.repository.EventRepository;
import com.abbos.moviego.service.CinemaHallService;
import com.abbos.moviego.service.EventService;
import com.abbos.moviego.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private CinemaHallService cinemaHallService;

    @Mock
    private MovieService movieService;

    @Mock
    private EventService self;

    @InjectMocks
    private EventServiceImpl eventService;

    private Event event;
    private Movie movie;
    private CinemaHall cinemaHall;
    private EventCreateDto eventCreateDto;
    private EventUpdateDto eventUpdateDto;
    private EventResponseDto eventResponseDto;
    private SimpleEventDto simpleEventDto;

    @BeforeEach
    void setUp() {
        movie = Movie.builder()
                .id(1L)
                .title("Test Movie")
                .durationMinutes(120)
                .language("English")
                .rating("PG-13")
                .trailerLink("https://movie.s3.amaozn.com/trailer")
                .description("A test movie")
                .build();

        cinemaHall = CinemaHall.builder()
                .id(1L)
                .name("Hall 1")
                .capacity(100)
                .build();

        event = Event.builder()
                .id(1L)
                .movie(movie)
                .cinemaHall(cinemaHall)
                .showTime(LocalDateTime.of(2025, 6, 1, 18, 0))
                .price(new BigDecimal("10.00"))
                .status(EventStatus.SCHEDULED)
                .capacity(100)
                .createdAt(LocalDateTime.now())
                .build();

        eventCreateDto = new EventCreateDto(
                1L,
                1L,
                LocalDateTime.of(2025, 6, 1, 18, 0),
                new BigDecimal("10.00")
        );

        eventUpdateDto = new EventUpdateDto(
                1L,
                EventStatus.COMPLETED
        );

        eventResponseDto = new EventResponseDto(
                1L,
                new MovieResponseDto(1L, "Test Movie", 120, "English", "PG-13", "https://amazon3.com/trailer", "A test movie", null, null, null, LocalDateTime.now()),
                new CinemaHallResponseDto(1L, "Hall 1", 100, 10, 10, null, null),
                LocalDateTime.of(2025, 6, 1, 18, 0),
                "10.00",
                EventStatus.SCHEDULED,
                100,
                LocalDateTime.now()
        );

        simpleEventDto = new SimpleEventDto(1L, LocalDateTime.of(2025, 6, 1, 18, 0));
    }

    @Test
    @DisplayName("Create event with valid EventCreateDto")
    void create_shouldSaveEventWhenValidDtoProvided() {
        when(eventMapper.fromCreate(eventCreateDto)).thenReturn(event);
        when(movieService.findEntity(1L)).thenReturn(movie);
        when(cinemaHallService.findEntity(1L)).thenReturn(cinemaHall);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        eventService.create(eventCreateDto);

        verify(eventMapper, times(1)).fromCreate(eventCreateDto);
        verify(movieService, times(1)).findEntity(1L);
        verify(cinemaHallService, times(1)).findEntity(1L);
        verify(eventRepository, times(1)).save(event);
        assertEquals(100, event.getCapacity());
        assertEquals(movie, event.getMovie());
        assertEquals(cinemaHall, event.getCinemaHall());
        verifyNoInteractions(self);
    }

    @Test
    @DisplayName("Update event status with valid EventUpdateDto")
    void update_shouldUpdateEventStatusWhenValidDtoProvided() {
        when(self.findEntity(1L)).thenReturn(event);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        eventService.update(eventUpdateDto);

        verify(self, times(1)).findEntity(1L);
        verify(eventRepository, times(1)).save(event);
        assertEquals(EventStatus.COMPLETED, event.getStatus());
        verifyNoInteractions(eventMapper, movieService, cinemaHallService);
    }

    @Test
    @DisplayName("Delete event when it exists")
    void delete_shouldDeleteEventWhenExists() {
        when(eventRepository.existsById(1L)).thenReturn(true);

        eventService.delete(1L);

        verify(eventRepository, times(1)).existsById(1L);
        verify(eventRepository, times(1)).deleteById(1L);
        verifyNoInteractions(eventMapper, movieService, cinemaHallService, self);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when deleting non-existent event")
    void delete_shouldThrowNotFoundWhenEventDoesNotExist() {
        when(eventRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> eventService.delete(1L));

        verify(eventRepository, times(1)).existsById(1L);
        verify(eventRepository, never()).deleteById(anyLong());
        verifyNoInteractions(eventMapper, movieService, cinemaHallService, self);
    }

    @Test
    @DisplayName("Mark completed events and return count")
    void markCompletedEvents_shouldReturnUpdatedCount() {
        when(eventRepository.markCompletedEvents()).thenReturn(5);

        int result = eventService.markCompletedEvents();

        verify(eventRepository, times(1)).markCompletedEvents();
        assertEquals(5, result);
        verifyNoInteractions(eventMapper, movieService, cinemaHallService, self);
    }

    @Test
    @DisplayName("Decrease event capacity and return updated count")
    void decreaseCapacity_shouldReturnUpdatedCount() {
        when(eventRepository.decreaseCapacity(1L)).thenReturn(1);

        int result = eventService.decreaseCapacity(1L);

        verify(eventRepository, times(1)).decreaseCapacity(1L);
        assertEquals(1, result);
        verifyNoInteractions(eventMapper, movieService, cinemaHallService, self);
    }

    @Test
    @DisplayName("Find simple event DTOs by movie ID")
    void findSimpleEventDtoByMovieId_shouldReturnEventDtos() {
        List<SimpleEventDto> dtos = List.of(simpleEventDto);
        when(eventRepository.findSimpleEventDtoByMovieId(1L)).thenReturn(dtos);

        List<SimpleEventDto> result = eventService.findSimpleEventDtoByMovieId(1L);

        verify(eventRepository, times(1)).findSimpleEventDtoByMovieId(1L);
        assertEquals(dtos, result);
        verifyNoInteractions(eventMapper, movieService, cinemaHallService, self);
    }

    @Test
    @DisplayName("Return true when event exists by ID")
    void exists_shouldReturnTrueWhenEventExists() {
        when(eventRepository.existsById(1L)).thenReturn(true);

        boolean result = eventService.exists(1L);

        verify(eventRepository, times(1)).existsById(1L);
        assertTrue(result);
        verifyNoInteractions(eventMapper, movieService, cinemaHallService, self);
    }

    @Test
    @DisplayName("Return false when event does not exist by ID")
    void exists_shouldReturnFalseWhenEventDoesNotExist() {
        when(eventRepository.existsById(1L)).thenReturn(false);

        boolean result = eventService.exists(1L);

        verify(eventRepository, times(1)).existsById(1L);
        assertFalse(result);
        verifyNoInteractions(eventMapper, movieService, cinemaHallService, self);
    }

    @Test
    @DisplayName("Find and return EventResponseDto for existing event")
    void find_shouldReturnEventDtoWhenEventExists() {
        when(self.findEntity(1L)).thenReturn(event);
        when(eventMapper.toDto(event)).thenReturn(eventResponseDto);

        EventResponseDto result = eventService.find(1L);

        verify(self, times(1)).findEntity(1L);
        verify(eventMapper, times(1)).toDto(event);
        assertEquals(eventResponseDto, result);
        verifyNoInteractions(eventRepository, movieService, cinemaHallService);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when finding non-existent event")
    void find_shouldThrowNotFoundWhenEventDoesNotExist() {
        when(self.findEntity(1L)).thenThrow(new ResourceNotFoundException("Event not found with id: 1"));

        assertThrows(ResourceNotFoundException.class, () -> eventService.find(1L));

        verify(self, times(1)).findEntity(1L);
        verify(eventMapper, never()).toDto(any(Event.class));
        verifyNoInteractions(eventRepository, movieService, cinemaHallService);
    }

    @Test
    @DisplayName("Find and return Event entity for existing event")
    void findEntity_shouldReturnEventWhenExists() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Event result = eventService.findEntity(1L);

        verify(eventRepository, times(1)).findById(1L);
        assertEquals(event, result);
        verifyNoInteractions(eventMapper, movieService, cinemaHallService, self);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when finding non-existent event entity")
    void findEntity_shouldThrowNotFoundWhenEventDoesNotExist() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventService.findEntity(1L));

        verify(eventRepository, times(1)).findById(1L);
        verifyNoInteractions(eventMapper, movieService, cinemaHallService, self);
    }

    @Test
    @DisplayName("Find all events by status ordered by show time")
    void findAllByStatus_shouldReturnEventDtosByStatus() {
        List<Event> events = List.of(event);
        List<EventResponseDto> dtos = List.of(eventResponseDto);
        when(eventRepository.findAllByStatusOrderByShowTimeAsc(EventStatus.SCHEDULED)).thenReturn(events);
        when(eventMapper.toDtoList(events)).thenReturn(dtos);

        List<EventResponseDto> result = eventService.findAllByStatus(EventStatus.SCHEDULED);

        verify(eventRepository, times(1)).findAllByStatusOrderByShowTimeAsc(EventStatus.SCHEDULED);
        verify(eventMapper, times(1)).toDtoList(events);
        assertEquals(dtos, result);
        verifyNoInteractions(movieService, cinemaHallService, self);
    }

    @Test
    @DisplayName("Return list of all EventResponseDto")
    void findAll_shouldReturnListOfEventDtos() {
        List<Event> events = List.of(event);
        List<EventResponseDto> dtos = List.of(eventResponseDto);
        when(eventRepository.findAll()).thenReturn(events);
        when(eventMapper.toDtoList(events)).thenReturn(dtos);

        List<EventResponseDto> result = eventService.findAll();

        verify(eventRepository, times(1)).findAll();
        verify(eventMapper, times(1)).toDtoList(events);
        assertEquals(dtos, result);
        verifyNoInteractions(movieService, cinemaHallService, self);
    }

    @Test
    @DisplayName("Return list of all EventResponseDto with eager loading")
    void findAllEager_shouldReturnListOfEventDtos() {
        List<Event> events = List.of(event);
        List<EventResponseDto> dtos = List.of(eventResponseDto);
        when(eventRepository.findAllEager()).thenReturn(events);
        when(eventMapper.toDtoList(events)).thenReturn(dtos);

        List<EventResponseDto> result = eventService.findAllEager();

        verify(eventRepository, times(1)).findAllEager();
        verify(eventMapper, times(1)).toDtoList(events);
        assertEquals(dtos, result);
        verifyNoInteractions(movieService, cinemaHallService, self);
    }
}
