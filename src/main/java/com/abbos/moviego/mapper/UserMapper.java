package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.UserResponseDto;
import com.abbos.moviego.dto.UserUpdateDto;
import com.abbos.moviego.dto.auth.SignUpDto;
import com.abbos.moviego.entity.User;
import com.abbos.moviego.mapper.base.EntityMapper;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ImageMapper.class, RoleMapper.class}
)
public interface UserMapper extends EntityMapper<User, UserResponseDto, SignUpDto, UserUpdateDto> {

    @Override
    @Mapping(target = "roles", ignore = true)
    User fromCreate(SignUpDto dto);

    @Mappings({
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    })
    User fromCreate(SignUpDto dto, @Context PasswordEncoder passwordEncoder);

    @Named("encodePassword")
    default String encodePassword(String rawPassword, @Context PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(rawPassword);
    }
}
