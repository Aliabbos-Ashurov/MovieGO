package com.abbos.moviego.mapper;


import com.abbos.moviego.dto.base.Request;
import com.abbos.moviego.entity.BaseDomain;

/**
 * Mapper interface for creating entities from create request DTOs.
 *
 * @param <E> the entity type extending {@link BaseDomain}
 * @param <C> the create request DTO type extending {@link Request}
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-02
 */
public interface CreateMapper<E extends BaseDomain, C extends Request> {
    /**
     * Creates an entity from a create request DTO.
     *
     * @param dto the create request DTO
     * @return the created entity
     */
    E fromCreate(C dto);
}
