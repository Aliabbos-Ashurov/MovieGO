package com.abbos.moviego.service.base;

import com.abbos.moviego.dto.base.Request;
import com.abbos.moviego.dto.base.Response;
import jakarta.validation.constraints.NotNull;

/**
 * Interface for entity creation operations.
 *
 * @param <R>  the response type
 * @param <CD> the create request type
 * @author Aliabbos Ashurov
 * @since 2025-05-06
 */
public interface CreateService<R extends Response, CD extends Request> {

    /**
     * Creates a new entity from the provided request data.
     *
     * @param dto the create request DTO
     * @return a response containing the created entity DTO
     * @throws jakarta.validation.ValidationException if the request is invalid
     */
    R create(@NotNull CD dto);
}
