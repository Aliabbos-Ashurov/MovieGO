package com.abbos.moviego.service.impl;

import com.abbos.moviego.config.security.SessionUser;
import com.abbos.moviego.dto.UserAddRoleDto;
import com.abbos.moviego.dto.UserResponseDto;
import com.abbos.moviego.dto.UserUpdateDto;
import com.abbos.moviego.dto.auth.SignUpDto;
import com.abbos.moviego.dto.internal.WelcomeEmailDto;
import com.abbos.moviego.entity.Image;
import com.abbos.moviego.entity.Role;
import com.abbos.moviego.entity.User;
import com.abbos.moviego.exception.ModificationNotAllowedException;
import com.abbos.moviego.exception.UserNotFoundException;
import com.abbos.moviego.mapper.UserMapper;
import com.abbos.moviego.messaging.producer.MessageProducer;
import com.abbos.moviego.repository.UserRepository;
import com.abbos.moviego.service.ImageService;
import com.abbos.moviego.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ImageService imageService;

    @Mock
    private SessionUser sessionUser;

    @Mock
    private MessageProducer messageProducer;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserResponseDto userResponseDto;
    private SignUpDto signUpDto;
    private UserUpdateDto userUpdateDto;
    private UserAddRoleDto userAddRoleDto;
    private Role role;
    private Image image;
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .fullname("Aliabbos Ashurov")
                .email("aliabbosashurov@gmail.com")
                .password("encodedPassword")
                .roles(new HashSet<>())
                .createdAt(LocalDateTime.now())
                .build();

        userResponseDto = new UserResponseDto(
                1L,
                "Aliabbos Ashurov",
                "aliabbosashurov@gmail.com",
                null,
                Collections.emptyList(),
                LocalDateTime.now()
        );

        signUpDto = new SignUpDto(
                "Aliabbos Ashurov",
                "aliabbosashurov@gmail.com",
                "password123"
        );

        userUpdateDto = new UserUpdateDto(
                1L,
                "oldPassword",
                "newPassword"
        );

        userAddRoleDto = new UserAddRoleDto(
                1L,
                Set.of(1L, 2L)
        );

        role = Role.builder()
                .id(1L)
                .name("USER")
                .permissions(new HashSet<>())
                .build();

        image = Image.builder()
                .id(1L)
                .generatedName("generated_profile_123")
                .fileName("profile.jpg")
                .extension("jpg")
                .size(1024L)
                .link("https://moviego.s3.amazon/moviego/profile.jpg")
                .build();

        multipartFile = mock(MultipartFile.class);
    }

    @Test
    @DisplayName("Create user with valid SignUpDto and send welcome email")
    void create_shouldSaveUserAndSendWelcomeEmail() {
        when(roleService.findByName("USER")).thenReturn(role);
        when(userMapper.fromCreate(any(SignUpDto.class), any(PasswordEncoder.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.create(signUpDto);

        verify(roleService, times(1)).findByName("USER");
        verify(userMapper, times(1)).fromCreate(signUpDto, passwordEncoder);
        verify(userRepository, times(1)).save(user);
        verify(messageProducer, times(1)).send(any(WelcomeEmailDto.class));
        verify(passwordEncoder, never()).encode(anyString());
        assertTrue(user.getRoles().contains(role));
    }

    @Test
    @DisplayName("Upload profile photo for authenticated user and return UserResponseDto")
    void uploadPhoto_shouldUpdateUserProfilePicAndReturnDto() {
        when(sessionUser.getId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(multipartFile.getOriginalFilename()).thenReturn("profile.jpg");
        when(imageService.create("profile", multipartFile)).thenReturn(image);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto result = userService.uploadPhoto(multipartFile);

        verify(sessionUser, times(1)).getId();
        verify(userRepository, times(1)).findById(1L);
        verify(multipartFile, times(1)).getOriginalFilename();
        verify(imageService, times(1)).create("profile", multipartFile);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(user);
        assertEquals(userResponseDto, result);
        assertEquals(image, user.getProfilePic());
    }

    @Test
    @DisplayName("Add roles to user with valid UserAddRoleDto")
    void addRole_shouldUpdateUserRoles() {
        Set<Role> roles = Set.of(role);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleService.findAllByIdIn(anySet())).thenReturn(roles);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.addRole(userAddRoleDto);

        verify(userRepository, times(1)).findById(1L);
        verify(roleService, times(1)).findAllByIdIn(userAddRoleDto.roles());
        verify(userRepository, times(1)).save(user);
        assertEquals(roles, user.getRoles());
    }

    @Test
    @DisplayName("Retrieve authenticated user's details")
    void getMe_shouldReturnCurrentUserDto() {
        when(sessionUser.getId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto result = userService.getMe();

        verify(sessionUser, times(1)).getId();
        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toDto(user);
        assertEquals(userResponseDto, result);
    }

    @Test
    @DisplayName("Update user password when old password matches")
    void update_shouldUpdatePasswordWhenOldPasswordMatches() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.update(userUpdateDto);

        verify(userRepository, times(1)).findById(1L);
        verify(passwordEncoder, times(1)).matches("oldPassword", "encodedPassword");
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(userRepository, times(1)).save(user);
        assertEquals("newEncodedPassword", user.getPassword());
    }

    @Test
    @DisplayName("Throw exception when updating password with incorrect old password")
    void update_shouldThrowExceptionWhenOldPasswordDoesNotMatch() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", "encodedPassword")).thenReturn(false);

        assertThrows(ModificationNotAllowedException.class, () -> userService.update(userUpdateDto));

        verify(userRepository, times(1)).findById(1L);
        verify(passwordEncoder, times(1)).matches("oldPassword", "encodedPassword");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Delete user when user exists")
    void delete_shouldDeleteUserWhenExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Throw UserNotFoundException when deleting non-existent user")
    void delete_shouldThrowNotFoundWhenUserDoesNotExist() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.delete(1L));

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Return true when user exists by ID")
    void exists_shouldReturnTrueWhenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean result = userService.exists(1L);

        verify(userRepository, times(1)).existsById(1L);
        assertTrue(result);
    }

    @Test
    @DisplayName("Return false when user does not exist by ID")
    void exists_shouldReturnFalseWhenUserDoesNotExist() {
        when(userRepository.existsById(1L)).thenReturn(false);

        boolean result = userService.exists(1L);

        verify(userRepository, times(1)).existsById(1L);
        assertFalse(result);
    }

    @Test
    @DisplayName("Find and return UserResponseDto for existing user")
    void find_shouldReturnUserDtoWhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto result = userService.find(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toDto(user);
        assertEquals(userResponseDto, result);
    }

    @Test
    @DisplayName("Throw UserNotFoundException when finding non-existent user")
    void find_shouldThrowNotFoundWhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.find(1L));

        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, never()).toDto(any(User.class));
    }

    @Test
    @DisplayName("Find and return User entity for existing user")
    void findEntity_shouldReturnUserWhenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findEntity(1L);

        verify(userRepository, times(1)).findById(1L);
        assertEquals(user, result);
    }

    @Test
    @DisplayName("Throw UserNotFoundException when finding non-existent user entity")
    void findEntity_shouldThrowNotFoundWhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findEntity(1L));

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Return list of UserResponseDto for all users")
    void findAll_shouldReturnListOfUserDtos() {
        List<User> users = List.of(user);
        List<UserResponseDto> dtos = List.of(userResponseDto);
        when(userRepository.findAll(any(Sort.class))).thenReturn(users);
        when(userMapper.toDtoList(users)).thenReturn(dtos);

        List<UserResponseDto> result = userService.findAll();

        verify(userRepository, times(1)).findAll(Sort.by(Sort.Direction.DESC, "id"));
        verify(userMapper, times(1)).toDtoList(users);
        assertEquals(dtos, result);
    }

    @Test
    @DisplayName("Find and return User by email when user exists")
    void findByEmail_shouldReturnUserWhenExists() {
        when(userRepository.findByEmail("aliabbosashurov@test.com")).thenReturn(Optional.of(user));

        User result = userService.findByEmail("aliabbosashurov@test.com");

        verify(userRepository, times(1)).findByEmail("aliabbosashurov@test.com");
        assertEquals(user, result);
    }

    @Test
    @DisplayName("Throw UserNotFoundException when finding user by non-existent email")
    void findByEmail_shouldThrowNotFoundWhenUserDoesNotExist() {
        when(userRepository.findByEmail("aliabbosashurov@test.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByEmail("aliabbosashurov@test.com"));

        verify(userRepository, times(1)).findByEmail("aliabbosashurov@test.com");
    }
}