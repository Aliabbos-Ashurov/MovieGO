package com.abbos.moviego.service.impl;

import com.abbos.moviego.config.security.SessionUser;
import com.abbos.moviego.dto.UserAddRoleDto;
import com.abbos.moviego.dto.UserResponseDto;
import com.abbos.moviego.dto.UserUpdateDto;
import com.abbos.moviego.dto.auth.SignUpDto;
import com.abbos.moviego.entity.Image;
import com.abbos.moviego.entity.Role;
import com.abbos.moviego.entity.User;
import com.abbos.moviego.exception.ModificationNotAllowedException;
import com.abbos.moviego.exception.UserNotFoundException;
import com.abbos.moviego.mapper.UserMapper;
import com.abbos.moviego.repository.UserRepository;
import com.abbos.moviego.service.ImageService;
import com.abbos.moviego.service.RoleService;
import com.abbos.moviego.service.UserService;
import com.abbos.moviego.service.base.AbstractService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
@Service
public class UserServiceImpl extends AbstractService<UserRepository, UserMapper> implements UserService {

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final SessionUser sessionUser;

    public UserServiceImpl(UserRepository repository,
                           UserMapper userMapper,
                           RoleService roleService,
                           @Lazy PasswordEncoder passwordEncoder,
                           ImageService imageService,
                           SessionUser sessionUser) {
        super(repository, userMapper);
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
        this.sessionUser = sessionUser;
    }

    @Transactional
    @Override
    public void create(SignUpDto dto) {
        Role role = roleService.findByName("USER");

        User user = mapper.fromCreate(dto, passwordEncoder);
        user.setRoles(Set.of(role));
        repository.save(user);
    }

    @Transactional
    @Override
    public UserResponseDto uploadPhoto(MultipartFile image) {
        Image userImage = imageService.create(FilenameUtils.getBaseName(image.getOriginalFilename()), image);

        User user = findEntity(sessionUser.getId());
        user.setProfilePic(userImage);
        return mapper.toDto(repository.save(user));
    }

    @Transactional
    @Override
    public void addRole(UserAddRoleDto dto) {
        User user = findEntity(dto.id());
        Set<Role> newRoles = roleService.findAllByIdIn(dto.roles());
        /*
         when performance need to only addRole!
         Set<Role> newRoles = roleService.findAllByIdIn(dto.roles());
         user.getRoles().addAll(newRoles);
         User saved = repository.save(user);
        */
        user.setRoles(newRoles);
        repository.save(user);
    }

    @Override
    public UserResponseDto getMe() {
        return find(sessionUser.getId());
    }

    @Transactional
    @Override
    public void update(UserUpdateDto dto) {
        User user = findEntity(dto.id());
        if (passwordEncoder.matches(dto.oldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(dto.newPassword()));
            repository.save(user);
        }
        throw new ModificationNotAllowedException("Password does not match!");
    }

    @Override
    public void delete(final Long id) {
        if (!exists(id)) {
            throwNotFound(id);
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
                () -> returnNotFound(id)
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
                () -> new UserNotFoundException("User not found with email: " + email)
        );
    }

    private void throwNotFound(Long id) {
        throw returnNotFound(id);
    }

    private UserNotFoundException returnNotFound(Long id) {
        return new UserNotFoundException("User not found with id: " + id);
    }
}
