package com.abbos.moviego.mapper.base;


import com.abbos.moviego.dto.base.Request;
import com.abbos.moviego.entity.base.BaseEntity;

/**
 * Mapper interface for creating entities from create request DTOs.
 *
 * @param <E> the entity type extending {@link BaseEntity}
 * @param <C> the create request DTO type extending {@link Request}
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-02
 */
public interface CreateMapper<E extends BaseEntity, C extends Request> {
    /**
     * Creates an entity from a create request DTO.
     *
     * @param dto the create request DTO
     * @return the created entity
     */
    E fromCreate(C dto);
}
