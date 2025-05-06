package com.abbos.moviego.mapper.base;

import com.abbos.moviego.dto.base.Request;
import com.abbos.moviego.entity.base.BaseEntity;
import org.mapstruct.MappingTarget;

/**
 * Mapper interface for updating entities from update request DTOs.
 * Supports both full updates and partial merges.
 *
 * @param <E> the entity type extending {@link BaseEntity}
 * @param <U> the update request DTO type extending {@link Request}
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-02
 */
public interface UpdateMapper<E extends BaseEntity, U extends Request> {
    /**
     * Creates an updated entity from an update request DTO.
     *
     * @param dto the update request DTO
     * @return the updated entity
     */
    E fromUpdate(U dto);

    /**
     * Merges updates from an update request DTO into an existing entity.
     *
     * @param target the entity to update
     * @param source the update request DTO containing new data
     * @return the updated entity
     */
    E mergeUpdate(@MappingTarget E target, U source);
}
