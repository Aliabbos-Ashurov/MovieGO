package com.abbos.moviego.service.base;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * Interface for entity deletion operations.
 *
 * @param <ID> the type of the entity ID
 * @author Aliabbos Ashurov
 * @since 2025-05-06
 */
public interface DeleteService<ID extends Serializable> {

    /**
     * Deletes an entity by its identifier.
     *
     * @param id the entity identifier
     */
    void delete(@NotNull ID id);
}
