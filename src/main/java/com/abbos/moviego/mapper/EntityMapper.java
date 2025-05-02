package com.abbos.moviego.mapper;


import com.abbos.moviego.dto.base.Request;
import com.abbos.moviego.dto.base.Response;
import com.abbos.moviego.entity.BaseDomain;

/**
 * Composite mapper interface combining entity conversion, creation, update, and collection mapping.
 * Acts as a facade for all mapping operations between entities and DTOs.
 *
 * @param <E> the entity type extending {@link BaseDomain}
 * @param <D> the response DTO type extending {@link Response}
 * @param <C> the create request DTO type extending {@link Request}
 * @param <U> the update request DTO type extending {@link Request}
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-02
 */
public interface EntityMapper<
        E extends BaseDomain,
        D extends Response,
        C extends Request,
        U extends Request>
        extends EntityConverter<E, D>, CreateMapper<E, C>, UpdateMapper<E, U>, CollectionMapper<E, D> {
}
