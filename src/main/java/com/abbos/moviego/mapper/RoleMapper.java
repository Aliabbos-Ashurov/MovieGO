package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.RoleCreateDto;
import com.abbos.moviego.dto.RoleResponseDto;
import com.abbos.moviego.dto.RoleUpdateDto;
import com.abbos.moviego.entity.Role;
import com.abbos.moviego.mapper.base.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = PermissionMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RoleMapper
        extends EntityMapper<Role, RoleResponseDto, RoleCreateDto, RoleUpdateDto> {

    @Override
    @Mapping(target = "permissions", ignore = true)
    Role fromCreate(RoleCreateDto dto);
}
