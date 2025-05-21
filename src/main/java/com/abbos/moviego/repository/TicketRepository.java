package com.abbos.moviego.repository;

import com.abbos.moviego.dto.internal.TicketRenderDto;
import com.abbos.moviego.entity.Ticket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public interface TicketRepository extends ListCrudRepository<Ticket, Long> {

    @Query("""
                SELECT new com.abbos.moviego.dto.internal.TicketRenderDto(
                           t.id,
                           t.rows,
                           t.columns,
                           t.price,
                           u.email,
                           m.title,
                           e.showTime,
                           ch.name)
                FROM Ticket t
                JOIN t.user u
                JOIN t.event e
                JOIN e.movie m
                JOIN e.cinemaHall ch
                WHERE t.id = :ticketId
            """)
    TicketRenderDto findTicketForRender(@Param("ticketId") Long ticketId);

    @Query("FROM Ticket t JOIN FETCH t.event e WHERE t.user.id = :userId")
    List<Ticket> findTicketsByUserId(Long userId);

    @Query("SELECT COUNT(t) > 0 FROM Ticket t WHERE t.event.id = :eventId AND t.rows = :row AND t.columns = :column")
    boolean existsByEventIdAndSeat(@Param("eventId") Long eventId,
                                   @Param("row") Integer row,
                                   @Param("column") Integer column);

    @Query("SELECT t FROM Ticket t WHERE t.event.id = :eventId")
    List<Ticket> findByEventId(@Param("eventId") Long eventId);

}
