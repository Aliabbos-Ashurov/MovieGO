package com.abbos.moviego.service.base;

import com.abbos.moviego.dto.base.Request;
import com.abbos.moviego.dto.base.Response;

import java.io.Serializable;

/**
 * Base CRUD service for managing entities.
 *
 * @param <ID> the type of the entity ID
 * @param <R>  the response type
 * @param <CD> the create Request type
 * @param <UD> the update Request type
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
public interface CrudService<
        ID extends Serializable,
        R extends Response,
        CD extends Request,
        UD extends Request
        > extends CreateService<R, CD>, UpdateService<R, UD>, DeleteService<ID>, SearchService<ID, R> {
}