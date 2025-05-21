package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.RoleCreateDto;
import com.abbos.moviego.dto.RoleResponseDto;
import com.abbos.moviego.dto.RoleUpdateDto;
import com.abbos.moviego.entity.Permission;
import com.abbos.moviego.entity.Role;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.RoleMapper;
import com.abbos.moviego.repository.RoleRepository;
import com.abbos.moviego.service.PermissionService;
import com.abbos.moviego.service.RoleService;
import com.abbos.moviego.service.base.AbstractService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-09
 */
@Service
public class RoleServiceImpl extends AbstractService<RoleRepository, RoleMapper> implements RoleService {

    private final PermissionService permissionService;

    public RoleServiceImpl(RoleRepository repository,
                           RoleMapper roleMapper,
                           PermissionService permissionService) {
        super(repository, roleMapper);
        this.permissionService = permissionService;
    }

    @Override
    public Role findByName(String name) {
        return repository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Role not found with name: " + name)
        );
    }

    @Transactional
    @Override
    public void create(RoleCreateDto dto) {
        Set<Permission> permissions = permissionService.findAllByIds(dto.permissions());
        if (dto.permissions().size() != permissions.size()) {
            throw new ResourceNotFoundException("Some requested permissions do not exist.");
        }
        Role role = Role.builder()
                .name(dto.name())
                .permissions(permissions)
                .build();
        repository.save(role);
    }

    @Override
    public void update(RoleUpdateDto dto) {
        Role role = findEntity(dto.id());
        role.setName(dto.name());

        repository.save(role);
    }

    @Override
    public Set<Role> findAllByIdIn(Set<Long> ids) {
        return repository.findAllByIdIn(ids);
    }


    @Override
    public void delete(Long id) {
        if (!exists(id)) {
            throw returnNotFound(id);
        }
        repository.deleteById(id);
    }

    @Override
    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    @Override
    public RoleResponseDto find(Long id) {
        return mapper.toDto(findEntity(id));
    }


    @Override
    public Role findEntity(Long id) {
        return repository.findById(id).orElseThrow(
                () -> returnNotFound(id)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<RoleResponseDto> findAll() {
        return mapper.toDtoList(
                repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
        );
    }

    private ResourceNotFoundException returnNotFound(Long id) {
        return new ResourceNotFoundException("Role not found with id: " + id);
    }
}
