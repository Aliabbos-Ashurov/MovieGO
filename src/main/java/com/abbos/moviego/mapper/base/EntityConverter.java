package com.abbos.moviego.mapper.base;

import com.abbos.moviego.dto.base.Response;
import com.abbos.moviego.entity.base.BaseEntity;

/**
 * Mapper interface for bidirectional conversion between domain entities and DTOs.
 *
 * @param <E> the entity type extending {@link BaseEntity}
 * @param <D> the DTO type extending {@link Response}
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-02
 */
public interface EntityConverter<E extends BaseEntity, D extends Response> {
    /**
     * Converts an entity to a DTO.
     *
     * @param entity the entity to convert
     * @return the converted DTO
     */
    D toDto(E entity);

    /**
     * Converts a DTO to an entity.
     *
     * @param dto the DTO to convert
     * @return the converted entity
     */
    E toEntity(D dto);
}