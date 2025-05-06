package com.abbos.moviego.service.base;


import com.abbos.moviego.dto.base.Response;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;

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

    /**
     * Retrieves all entities with pagination support.
     * <p>
     * This default implementation throws {@link UnsupportedOperationException}
     * and should be overridden in the implementing class if pagination is required.
     *
     * @param pageable pagination and sorting information
     * @return a paginated list of response DTOs
     */
    default List<R> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("::Method must be overridden::");
    }
}