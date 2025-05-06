package com.abbos.moviego.service.base;


import com.abbos.moviego.dto.base.Response;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for read-only operations on entities.
 *
 * @param <ID> the type of the entity ID
 * @param <R>  the response type
 * @author Aliabbos Ashurov
 * @since 2025-05-06
 */
public interface SearchService<ID extends Serializable, R extends Response> {

    /**
     * Retrieves an entity by its identifier.
     *
     * @param id the entity identifier
     * @return a response containing the entity DTO
     * @throws com.abbos.moviego.exception.ResourceNotFoundException if the entity is not found
     */
    R find(@NotNull ID id);

    /**
     * Retrieves all entities with pagination support.
     *
     * @return a list of response DTOs containing entity data
     */
    List<R> findAll();
}