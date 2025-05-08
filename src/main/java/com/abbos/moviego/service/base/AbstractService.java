package com.abbos.moviego.service.base;

import com.abbos.moviego.mapper.base.EntityConverter;
import com.abbos.moviego.mapper.base.EntityMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Abstract base service class providing common functionality for services,
 * including repository access and entity mapping.
 *
 * @param <S> the type of the service {@link CrudService}
 * @param <R> the type of the repository extending {@link JpaRepository}
 * @param <M> the type of the mapper extending {@link EntityMapper}
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
@Getter
@RequiredArgsConstructor
public abstract class AbstractService<R extends JpaRepository<?, ?>, M extends EntityConverter<?, ?>> {
    protected final R repository;
    protected final M mapper;
}

