package com.abbos.moviego.repository;

import com.abbos.moviego.dto.SimpleEventDto;
import com.abbos.moviego.entity.Event;
import com.abbos.moviego.enums.EventStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public interface EventRepository extends ListCrudRepository<Event, Long> {

    @EntityGraph(attributePaths = { "movie", "movie.posterImage" })
    List<Event> findAllByStatusOrderByShowTimeAsc(EventStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.capacity = e.capacity - 1 WHERE e.id = :eventId AND e.capacity > 0")
    int decreaseCapacity(@Param("eventId") Long eventId);

    @Modifying
    @Query("UPDATE Event e SET e.status = 'COMPLETED' WHERE e.showTime < CURRENT_TIMESTAMP")
    int markCompletedEvents();

    @Query("""
                SELECT new com.abbos.moviego.dto.SimpleEventDto(e.id, e.showTime)
                FROM Event e
                WHERE e.movie.id = :movieId AND e.status = 'SCHEDULED'
                ORDER BY e.showTime ASC
            """)
    List<SimpleEventDto> findSimpleEventDtoByMovieId(Long movieId);

    @Query("""
                SELECT DISTINCT e FROM Event e
                JOIN FETCH e.movie m
                JOIN FETCH m.category c
                JOIN FETCH m.posterImage pi
                JOIN FETCH e.cinemaHall ch
                LEFT JOIN FETCH m.sceneImages si
                LEFT JOIN FETCH si.image img
                ORDER BY e.id DESC
            """)
    List<Event> findAllEager();
}
