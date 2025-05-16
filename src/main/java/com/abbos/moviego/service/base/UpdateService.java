package com.abbos.moviego.service.base;

import com.abbos.moviego.dto.base.Request;
import com.abbos.moviego.dto.base.Response;
import jakarta.validation.constraints.NotNull;

/**
 * Interface for entity update operations.
 *
 * @param <R>  the response type
 * @param <UD> the update request type
 * @author Aliabbos Ashurov
 * @since 2025-05-06
 */
public interface UpdateService<R extends Response, UD extends Request> {

    /**
     * Updates an existing entity with the provided request data.
     *
     * @param dto the update request DTO
     * @return a response containing the updated entity DTO
     * @throws com.abbos.moviego.exception.ResourceNotFoundException if the entity is not found
     */
    void update(@NotNull UD dto); // must be R (dto) in REST
}