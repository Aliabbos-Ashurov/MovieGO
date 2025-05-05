package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.PermissionCreateDto;
import com.abbos.moviego.dto.PermissionResponseDto;
import com.abbos.moviego.dto.PermissionUpdateDto;
import com.abbos.moviego.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        typeConversionPolicy = ReportingPolicy.WARN,  // when mapstruct integer to long, char to string ...
        unmappedTargetPolicy = ReportingPolicy.IGNORE) // sometimes id, createdAt,createdBy!
public interface PermissionMapper
        extends EntityMapper<Permission, PermissionResponseDto, PermissionCreateDto, PermissionUpdateDto> {
}
