package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.EventCreateDto;
import com.abbos.moviego.dto.EventResponseDto;
import com.abbos.moviego.dto.EventUpdateDto;
import com.abbos.moviego.entity.CinemaHall;
import com.abbos.moviego.entity.Event;
import com.abbos.moviego.entity.Image;
import com.abbos.moviego.entity.Movie;
import com.abbos.moviego.enums.EventStatus;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.EventMapper;
import com.abbos.moviego.repository.EventRepository;
import com.abbos.moviego.service.CinemaHallService;
import com.abbos.moviego.service.EventService;
import com.abbos.moviego.service.ImageService;
import com.abbos.moviego.service.MovieService;
import com.abbos.moviego.service.base.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-07
 */
@Service
public class EventServiceImpl extends AbstractService<EventRepository, EventMapper> implements EventService {

    private final CinemaHallService cinemaHallService;
    private final MovieService movieService;
    private final ImageService imageService;

    public EventServiceImpl(EventRepository repository,
                            EventMapper eventMapper,
                            CinemaHallService cinemaHallService,
                            MovieService movieService,
                            ImageService imageService) {
        super(repository, eventMapper);
        this.cinemaHallService = cinemaHallService;
        this.movieService = movieService;
        this.imageService = imageService;
    }

    @Transactional
    @Override
    public EventResponseDto create(EventCreateDto dto) {
        Event event = mapper.fromCreate(dto);

        Movie movie = movieService.findEntity(dto.movieId());
        CinemaHall cinemaHall = cinemaHallService.findEntity(dto.cinemaHallId());

        Image image = imageService.create("event", dto.banner());

        event.setCapacity(cinemaHall.getCapacity());
        event.setBanner(image);
        event.setMovie(movie);
        event.setCinemaHall(cinemaHall);

        return mapper.toDto(repository.save(event));
    }

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

    @Override
    public EventResponseDto find(Long id) {
        return mapper.toDto(findEntity(id));
    }

    @Override
    public Event findEntity(final Long id) {
        return repository.findById(id).orElseThrow(
                () -> returnNotFound(id)
        );
    }

    @Override
    public List<EventResponseDto> findAll() {
        return mapper.toDtoList(
                repository.findAll()
        );
    }

    @Transactional
    @Override
    public EventResponseDto update(EventUpdateDto dto) {
        Event event = findEntity(dto.id());
        event.setStatus(dto.status());
        return mapper.toDto(repository.save(event));
    }

    @Override
    public List<EventResponseDto> findEventsByCinemaHallId(Long cinemaHallId) {
        return mapper.toDtoList(
                repository.findEventsByCinemaHallId(cinemaHallId)
        );
    }

    @Override
    public List<EventResponseDto> findEventsByMovieId(Long movieId) {
        return mapper.toDtoList(
                repository.findEventsByMovieId(movieId)
        );
    }

    @Override
    public List<EventResponseDto> findEventsByStatus(EventStatus status) {
        return mapper.toDtoList(
                repository.findEventsByStatus(status)
        );
    }

    private ResourceNotFoundException returnNotFound(Long id) {
        return new ResourceNotFoundException("Event not found with id: {}", id);
    }

    private void throwNotFound(Long id) {
        throw new ResourceNotFoundException("Event not found with id: {}", id);
    }
}
