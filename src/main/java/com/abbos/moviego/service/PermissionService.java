package com.abbos.moviego.service;

import com.abbos.moviego.dto.PermissionCreateDto;
import com.abbos.moviego.dto.PermissionResponseDto;
import com.abbos.moviego.dto.PermissionUpdateDto;
import com.abbos.moviego.entity.Permission;
import com.abbos.moviego.service.base.CrudService;

import java.util.Set;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-11
 */
public interface PermissionService
        extends CrudService<Long, Permission, PermissionResponseDto, PermissionCreateDto, PermissionUpdateDto> {

    Set<Permission> findAllByIds(Set<Long> permissionIds);
}
