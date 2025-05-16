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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-11
 */
@Service
public class PermissionServiceImpl extends AbstractService<PermissionRepository, PermissionMapper> implements PermissionService {

    public PermissionServiceImpl(PermissionRepository repository, PermissionMapper permissionMapper) {
        super(repository, permissionMapper);
    }

    @Override
    public void create(PermissionCreateDto dto) {
        Permission permission = mapper.fromCreate(dto);
        repository.save(permission);
    }

    @Override
    public void update(PermissionUpdateDto dto) {
        Permission permission = mapper.fromUpdate(dto);
        repository.save(permission);
    }

    @Override
    public Set<Permission> findAllByIds(Set<Long> permissionIds) {
        return repository.findAllByIds(permissionIds);
    }

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
        return mapper.toDto(findEntity(id));
    }

    @Override
    public Permission findEntity(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Permission not found with id:" + id)
        );
    }

    @Override
    public List<PermissionResponseDto> findAll() {
        return mapper.toDtoList(
                repository.findAll()
        );
    }
}
