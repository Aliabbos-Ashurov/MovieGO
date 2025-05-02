package com.abbos.moviego.mapper;


import com.abbos.moviego.dto.base.Response;
import com.abbos.moviego.entity.BaseDomain;

import java.util.Collection;
import java.util.List;

/**
 * Mapper interface for converting between collections of domain entities and DTOs.
 * Provides methods to transform collections of DTOs to entities and vice versa.
 *
 * @param <E> the entity type extending {@link BaseDomain}
 * @param <D> the DTO type extending {@link Response}
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-02
 */
public interface CollectionMapper<E extends BaseDomain, D extends Response> {
    /**
     * Converts a collection of DTOs to a list of entities.
     *
     * @param collection the collection of DTOs to convert
     * @return a list of entities
     */
    List<E> toEntityList(Collection<D> collection);

    /**
     * Converts a collection of entities to a list of DTOs.
     *
     * @param entities the collection of entities to convert
     * @return a list of DTOs
     */
    List<D> toDtoList(Collection<E> entities);
}
