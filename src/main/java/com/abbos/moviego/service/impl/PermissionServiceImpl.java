package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.PermissionCreateDto;
import com.abbos.moviego.dto.PermissionResponseDto;
import com.abbos.moviego.dto.PermissionUpdateDto;
import com.abbos.moviego.entity.Permission;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.PermissionMapper;
import com.abbos.moviego.repository.PermissionRepository;
import com.abbos.moviego.service.PermissionService;
import com.abbos.moviego.service.base.AbstractService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.abbos.moviego.util.CacheKeys.FIND_ALL;
import static com.abbos.moviego.util.CacheKeys.PermissionKeys.PERMISSION;
import static com.abbos.moviego.util.CacheKeys.PermissionKeys.PERMISSIONS;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-11
 */
@Service
public class PermissionServiceImpl extends AbstractService<PermissionRepository, PermissionMapper> implements PermissionService {

    private final PermissionService self;

    public PermissionServiceImpl(PermissionRepository repository,
                                 PermissionMapper permissionMapper,
                                 @Lazy PermissionService permissionService) {
        super(repository, permissionMapper);
        this.self = permissionService;
    }

    @CacheEvict(value = PERMISSIONS, key = FIND_ALL)
    @Override
    public void create(PermissionCreateDto dto) {
        Permission permission = mapper.fromCreate(dto);
        repository.save(permission);
    }

    @Caching(evict = {
            @CacheEvict(value = PERMISSION, key = "#dto.id()"),
            @CacheEvict(value = PERMISSIONS, key = FIND_ALL)
    })
    @Override
    public void update(PermissionUpdateDto dto) {
        Permission permission = mapper.fromUpdate(dto);
        repository.save(permission);
    }

    @Override
    public Set<Permission> findAllByIds(Set<Long> permissionIds) {
        return repository.findAllByIds(permissionIds);
    }

    @Caching(evict = {
            @CacheEvict(value = PERMISSION, key = "#id"),
            @CacheEvict(value = PERMISSIONS, key = FIND_ALL)
    })
    @Override
    public void delete(Long id) {
        if (!exists(id)) {
            throw new ResourceNotFoundException("Permission not found with id:" + id);
        }
        repository.deleteById(id);
    }

    @Override
    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    @Override
    public PermissionResponseDto find(Long id) {
        return mapper.toDto(self.findEntity(id));
    }

    @Cacheable(value = PERMISSION, key = "#id")
    @Override
    public Permission findEntity(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Permission not found with id:" + id)
        );
    }

    @Cacheable(value = PERMISSIONS, key = FIND_ALL)
    @Override
    public List<PermissionResponseDto> findAll() {
        return mapper.toDtoList(
                repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
        );
    }
}
