package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.EventCreateDto;
import com.abbos.moviego.dto.EventResponseDto;
import com.abbos.moviego.dto.EventUpdateDto;
import com.abbos.moviego.dto.SimpleEventDto;
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
import com.abbos.moviego.service.base.AbstractService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.abbos.moviego.util.CacheKeys.EventKeys.EVENT;
import static com.abbos.moviego.util.CacheKeys.EventKeys.EVENTS;
import static com.abbos.moviego.util.CacheKeys.FIND_ALL;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-07
 */
@Service
public class EventServiceImpl extends AbstractService<EventRepository, EventMapper> implements EventService {

    private final CinemaHallService cinemaHallService;
    private final MovieService movieService;
    private final EventService self;

    public EventServiceImpl(EventRepository repository,
                            EventMapper eventMapper,
                            CinemaHallService cinemaHallService,
                            MovieService movieService,
                            @Lazy EventService eventService) {
        super(repository, eventMapper);
        this.cinemaHallService = cinemaHallService;
        this.movieService = movieService;
        this.self = eventService;
    }

    @CacheEvict(value = EVENTS, key = FIND_ALL)
    @Transactional
    @Override
    public void create(EventCreateDto dto) {
        Event event = mapper.fromCreate(dto);

        Movie movie = movieService.findEntity(dto.movieId());
        CinemaHall cinemaHall = cinemaHallService.findEntity(dto.cinemaHallId());

        event.setCapacity(cinemaHall.getCapacity());
        event.setMovie(movie);
        event.setCinemaHall(cinemaHall);

        repository.save(event);
    }

    @Caching(evict = {
            @CacheEvict(value = EVENT, key = "#dto.id()"),
            @CacheEvict(value = EVENTS, key = FIND_ALL)
    })
    @Transactional
    @Override
    public void update(EventUpdateDto dto) {
        Event event = self.findEntity(dto.id());
        event.setStatus(dto.status());
        repository.save(event);
    }

    @Caching(evict = {
            @CacheEvict(value = EVENT, key = "#id"),
            @CacheEvict(value = EVENTS, key = FIND_ALL)
    })
    @Override
    public void delete(Long id) {
        if (!exists(id)) {
            throwNotFound(id);
        }
        repository.deleteById(id);
    }


    @Override
    public int markCompletedEvents() {
        return repository.markCompletedEvents();
    }

    @Override
    public int decreaseCapacity(Long eventId) {
        return repository.decreaseCapacity(eventId);
    }

    @Override
    public List<SimpleEventDto> findSimpleEventDtoByMovieId(Long movieId) {
        return repository.findSimpleEventDtoByMovieId(movieId);
    }

    @Override
    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    @Override
    public EventResponseDto find(Long id) {
        return mapper.toDto(self.findEntity(id));
    }

    @Cacheable(value = EVENT, key = "#id")
    @Override
    public Event findEntity(final Long id) {
        return repository.findById(id).orElseThrow(
                () -> returnNotFound(id)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventResponseDto> findAllByStatus(EventStatus status) {
        return mapper.toDtoList(
                repository.findAllByStatusOrderByShowTimeAsc(status)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventResponseDto> findAll() {
        return mapper.toDtoList(
                repository.findAll()
        );
    }

    @Cacheable(value = EVENTS, key = FIND_ALL)
    @Transactional(readOnly = true)
    @Override
    public List<EventResponseDto> findAllEager() {
        return mapper.toDtoList(
                repository.findAllEager()
        );
    }

    private void throwNotFound(Long id) {
        throw returnNotFound(id);
    }

    private ResourceNotFoundException returnNotFound(Long id) {
        return new ResourceNotFoundException("Event not found with id: " + id);
    }
}
