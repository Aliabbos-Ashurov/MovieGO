package com.abbos.moviego.service;

import com.abbos.moviego.dto.RoleCreateDto;
import com.abbos.moviego.dto.RoleResponseDto;
import com.abbos.moviego.dto.RoleUpdateDto;
import com.abbos.moviego.entity.Role;
import com.abbos.moviego.service.base.CrudService;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-09
 */
public interface RoleService extends CrudService<Long, Role, RoleResponseDto, RoleCreateDto, RoleUpdateDto> {

    Role findByName(@NotNull String name);

    Set<Role> findAllByIdIn(Set<Long> ids);
}
