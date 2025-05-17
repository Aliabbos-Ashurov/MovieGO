package com.abbos.moviego.service;

import com.abbos.moviego.dto.EventCreateDto;
import com.abbos.moviego.dto.EventResponseDto;
import com.abbos.moviego.dto.EventUpdateDto;
import com.abbos.moviego.dto.SimpleEventDto;
import com.abbos.moviego.entity.Event;
import com.abbos.moviego.enums.EventStatus;
import com.abbos.moviego.service.base.CrudService;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
public interface EventService extends CrudService<Long, Event, EventResponseDto, EventCreateDto, EventUpdateDto> {

    List<EventResponseDto> findAllByStatus(EventStatus status);

    int decreaseCapacity(Long eventId);

    int markCompletedEvents();


    List<EventResponseDto> findAllEager();

    List<SimpleEventDto> findSimpleEventDtoByMovieId(Long movieId);
}
