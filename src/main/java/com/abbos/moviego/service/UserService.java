package com.abbos.moviego.service;

import com.abbos.moviego.dto.UserCreateDto;
import com.abbos.moviego.dto.UserResponseDto;
import com.abbos.moviego.dto.UserUpdateDto;
import com.abbos.moviego.entity.User;
import com.abbos.moviego.service.base.CrudService;

import java.util.Optional;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
public interface UserService
        extends CrudService<Long, UserResponseDto, UserCreateDto, UserUpdateDto> {

    Optional<User> findByEmail(String email);
}
