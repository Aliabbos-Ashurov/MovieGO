package com.abbos.moviego.service;

import com.abbos.moviego.dto.base.Request;
import com.abbos.moviego.dto.base.Response;
import com.abbos.moviego.entity.BaseEntity;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * Base CRUD service for managing entities.
 *
 * @param <ID> the type of the entity ID
 * @param <E>  the entity type
 * @param <R>  the response type
 * @param <CD> the create Request type
 * @param <UD> the update Request type
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
public interface CrudService<
        ID extends Serializable,
        E extends BaseEntity,
        R extends Response,
        CD extends Request,
        UD extends Request
        > {

    /**
     * Creates a new entity from the provided DTO.
     *
     * @param dto the create DTO
     * @return a response containing the created entity DTO
     * @throws jakarta.validation.ValidationException if the DTO is invalid
     */
    R create(@NotNull CD dto);

    /**
     * Retrieves an entity by ID.
     *
     * @param id the entity ID
     * @return a response containing the entity DTO
     * @throws com.abbos.moviego.exception.ResourceNotFoundException if the entity is not found
     */
    R find(@NotNull ID id);

    /**
     * Retrieves all entities with pagination.
     *
     * @return a response containing a list of entity DTOs
     */
    List<R> findAll();

    /**
     * Updates an existing entity.
     *
     * @param dto the update DTO
     * @return a response containing the updated entity DTO
     * @throws com.abbos.moviego.exception.ResourceNotFoundException if the entity is not found
     */
    R update(@NotNull UD dto);

    /**
     * Deletes an entity by ID.
     *
     * @param id the entity ID
     * @return a response indicating success
     * @throws com.abbos.moviego.exception.ResourceNotFoundException if the entity is not found
     */
    void delete(@NotNull ID id);
}