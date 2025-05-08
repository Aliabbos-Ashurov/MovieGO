package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.UserCreateDto;
import com.abbos.moviego.dto.UserResponseDto;
import com.abbos.moviego.dto.UserUpdateDto;
import com.abbos.moviego.entity.User;
import com.abbos.moviego.exception.UserNotFoundException;
import com.abbos.moviego.mapper.UserMapper;
import com.abbos.moviego.repository.UserRepository;
import com.abbos.moviego.service.UserService;
import com.abbos.moviego.service.base.AbstractService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
@Service
public class UserServiceImpl extends AbstractService<UserRepository, UserMapper> implements UserService {

    public UserServiceImpl(UserRepository repository, UserMapper userMapper) {
        super(repository, userMapper);
    }

    @Override
    public UserResponseDto create(UserCreateDto dto) {
        final User user = mapper.fromCreate(dto);
        final User saved = repository.save(user);
        return mapper.toDto(saved);
    }

    @Override
    public UserResponseDto update(UserUpdateDto dto) {
        final Long id = dto.id();
        if (!exists(id)) {
            throw new UserNotFoundException("User not found with id: {}", id);
        }
        User user = mapper.fromUpdate(dto);
        User save = repository.save(user);
        return mapper.toDto(save);
    }

    @Override
    public void delete(final Long id) {
        if (!exists(id)) {
            throw new UserNotFoundException("User not found with id: {} ", id);
        }
        repository.deleteById(id);
    }

    @Override
    public boolean exists(final Long id) {
        return repository.existsById(id);
    }

    @Override
    public UserResponseDto find(Long id) {
        return mapper.toDto(findEntity(id));
    }

    @Override
    public User findEntity(final Long id) {
        return repository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: {}", id)
        );
    }

    @Override
    public List<UserResponseDto> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public List<UserResponseDto> findUsersByNameLike(String name) {
        return mapper.toDtoList(repository.findUsersByNameLike(name));
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User not found with email: {}", email)
        );
    }
}
