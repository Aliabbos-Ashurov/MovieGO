package com.abbos.moviego.service;

import com.abbos.moviego.entity.BaseEntity;
import com.abbos.moviego.mapper.EntityConverter;
import com.abbos.moviego.mapper.EntityMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Abstract base service class providing common functionality for services,
 * including repository access and entity mapping.
 *
 * @param <R> the type of the repository extending {@link JpaRepository}
 * @param <M> the type of the mapper extending {@link EntityMapper}
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
@Getter
@RequiredArgsConstructor
public abstract class AbstractService<
        S extends CrudService<Long, ?, ?, ?, ?>,
        R extends JpaRepository<? extends BaseEntity, Long>,
        M extends EntityConverter<?, ?>> {
    private final S service;
    private final R repository;
    private final M mapper;
}

