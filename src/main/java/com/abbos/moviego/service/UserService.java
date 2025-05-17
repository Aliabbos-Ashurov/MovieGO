package com.abbos.moviego.service;

import com.abbos.moviego.dto.UserAddRoleDto;
import com.abbos.moviego.dto.UserResponseDto;
import com.abbos.moviego.dto.UserUpdateDto;
import com.abbos.moviego.dto.auth.SignUpDto;
import com.abbos.moviego.entity.User;
import com.abbos.moviego.service.base.CrudService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
public interface UserService extends CrudService<Long, User, UserResponseDto, SignUpDto, UserUpdateDto> {

    User findByEmail(String email);

    UserResponseDto uploadPhoto(MultipartFile image);

    UserResponseDto getMe();

    void addRole(UserAddRoleDto dto);

}
