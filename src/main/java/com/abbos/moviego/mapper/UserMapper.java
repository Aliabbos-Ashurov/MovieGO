package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.UserCreateDto;
import com.abbos.moviego.dto.UserResponseDto;
import com.abbos.moviego.dto.UserUpdateDto;
import com.abbos.moviego.entity.User;
import com.abbos.moviego.mapper.base.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ImageMapper.class, RoleMapper.class})
public interface UserMapper extends EntityMapper<User, UserResponseDto, UserCreateDto, UserUpdateDto> {
}
