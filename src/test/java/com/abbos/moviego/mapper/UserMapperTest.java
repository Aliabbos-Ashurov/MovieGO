package com.abbos.moviego.mapper;


import com.abbos.moviego.dto.*;
import com.abbos.moviego.dto.auth.SignUpDto;
import com.abbos.moviego.entity.Image;
import com.abbos.moviego.entity.Role;
import com.abbos.moviego.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@SpringBootTest(classes = {
        UserMapperImpl.class,
        ImageMapperImpl.class,
        RoleMapperImpl.class,
        PermissionMapperImpl.class
})
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testToDto() {
        User entity = UserBuilder.defaultUser();
        ImageResponseDto imageDto = new ImageResponseDto(1L, "profile_1", "profile.jpg", "jpg", 1024, "/images/profile_1.jpg");

        UserResponseDto dto = userMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getFullname(), dto.fullname());
        assertEquals(entity.getEmail(), dto.email());
        assertEquals(imageDto, dto.profilePic());
        assertEquals(1, dto.roles().size());
        assertEquals(entity.getCreatedAt(), dto.createdAt());
    }

    @Test
    void testToDto_NullInput() {
        UserResponseDto dto = userMapper.toDto(null);
        assertNull(dto);
    }

    @Test
    void testToEntity() {
        UserResponseDto dto = DtoBuilder.defaultUserResponseDto();
        User entity = userMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.fullname(), entity.getFullname());
        assertEquals(dto.email(), entity.getEmail());
        assertEquals(1, entity.getRoles().size());
        assertEquals(dto.createdAt(), entity.getCreatedAt());
    }

    @Test
    void testToEntity_NullInput() {
        User entity = userMapper.toEntity(null);
        assertNull(entity);
    }

    @Test
    void testToDtoList() {
        List<User> entities = List.of(UserBuilder.defaultUser());
        ImageResponseDto imageDto = new ImageResponseDto(1L, "profile_1", "profile.jpg", "jpg", 1024, "/images/profile_1.jpg");

        List<UserResponseDto> dtos = userMapper.toDtoList(entities);

        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        UserResponseDto dto = dtos.getFirst();
        User entity = entities.getFirst();
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getFullname(), dto.fullname());
        assertEquals(entity.getEmail(), dto.email());
        assertEquals(imageDto, dto.profilePic());
        assertEquals(1, dto.roles().size());
        assertEquals(entity.getCreatedAt(), dto.createdAt());
    }

    @Test
    void testToDtoList_NullInput() {
        List<UserResponseDto> dtos = userMapper.toDtoList(null);
        assertNull(dtos);
    }

    @Test
    void testToEntityList() {
        List<UserResponseDto> dtos = List.of(DtoBuilder.defaultUserResponseDto());
        List<User> entities = userMapper.toEntityList(dtos);

        assertNotNull(entities);
        assertEquals(1, entities.size());
        User entity = entities.getFirst();
        UserResponseDto dto = dtos.getFirst();
        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.fullname(), entity.getFullname());
        assertEquals(dto.email(), entity.getEmail());
        assertEquals(1, entity.getRoles().size());
        assertEquals(dto.createdAt(), entity.getCreatedAt());
    }

    @Test
    void testToEntityList_NullInput() {
        List<User> entities = userMapper.toEntityList(null);
        assertNull(entities);
    }

    @Test
    void testFromCreate() {
        SignUpDto dto = DtoBuilder.defaultSignUpDto();

        User entity = userMapper.fromCreate(dto);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals(dto.fullname(), entity.getFullname());
        assertEquals(dto.email(), entity.getEmail());
        assertEquals(dto.password(), entity.getPassword());
        assertTrue(entity.getRoles().isEmpty());
        assertNull(entity.getProfilePic());
    }

    @Test
    void testFromCreate_NullInput() {
        User entity = userMapper.fromCreate(null);
        assertNull(entity);
    }

    @Test
    void testFromCreateWithPasswordEncoder() {
        SignUpDto dto = DtoBuilder.defaultSignUpDto();
        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode(dto.password())).thenReturn(encodedPassword);

        User entity = userMapper.fromCreate(dto, passwordEncoder);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals(dto.fullname(), entity.getFullname());
        assertEquals(dto.email(), entity.getEmail());
        assertEquals(encodedPassword, entity.getPassword());
        assertTrue(entity.getRoles().isEmpty());
        assertNull(entity.getProfilePic());
    }

    @Test
    void testFromCreateWithPasswordEncoder_NullInput() {
        User entity = userMapper.fromCreate(null, passwordEncoder);
        assertNull(entity);
    }

    @Test
    void testFromUpdate() {
        UserUpdateDto dto = DtoBuilder.defaultUserUpdateDto();

        User entity = userMapper.fromUpdate(dto);

        assertNotNull(entity);
        assertEquals(dto.id(), entity.getId());
        assertNull(entity.getFullname());
        assertNull(entity.getEmail());
        assertNull(entity.getPassword());
        assertNull(entity.getProfilePic());
        assertTrue(entity.getRoles().isEmpty());
    }

    @Test
    void testFromUpdate_NullInput() {
        User entity = userMapper.fromUpdate(null);
        assertNull(entity);
    }

    @Test
    void testMergeUpdate() {
        UserUpdateDto dto = DtoBuilder.defaultUserUpdateDto();
        User target = UserBuilder.defaultUser();

        User result = userMapper.mergeUpdate(target, dto);

        assertNotNull(result);
        assertEquals(dto.id(), result.getId());
        assertEquals(target.getFullname(), result.getFullname());
        assertEquals(target.getEmail(), result.getEmail());
        assertEquals(target.getPassword(), result.getPassword());
        assertEquals(target.getProfilePic(), result.getProfilePic());
        assertEquals(target.getRoles(), result.getRoles());
    }

    @Test
    void testMergeUpdate_NullSource() {
        User target = UserBuilder.defaultUser();
        User result = userMapper.mergeUpdate(target, null);
        assertSame(target, result);
    }

    static class UserBuilder {
        static User defaultUser() {
            Role role = Role.builder()
                    .id(1L)
                    .name("USER")
                    .build();
            role.setCreatedAt(LocalDateTime.now());

            Image image = ImageBuilder.defaultImage();

            User user = User.builder()
                    .id(1L)
                    .fullname("Test User")
                    .email("test@example.com")
                    .password("password123")
                    .profilePic(image)
                    .roles(new HashSet<>(Set.of(role)))
                    .build();
            user.setCreatedAt(LocalDateTime.now());
            return user;
        }
    }

    static class ImageBuilder {
        static Image defaultImage() {
            Image image = Image.builder()
                    .id(1L)
                    .generatedName("profile_1")
                    .fileName("profile.jpg")
                    .extension("jpg")
                    .size(1024)
                    .link("/images/profile_1.jpg")
                    .build();
            image.setCreatedAt(LocalDateTime.now());
            return image;
        }
    }

    static class DtoBuilder {
        static UserResponseDto defaultUserResponseDto() {
            ImageResponseDto imageDto = new ImageResponseDto(1L, "profile_1", "profile.jpg", "jpg", 1024, "/images/profile_1.jpg");
            RoleResponseDto roleDto = new RoleResponseDto(1L, "USER", Set.of(new PermissionResponseDto(1L, "VIEW")));
            return new UserResponseDto(
                    1L,
                    "Test User",
                    "test@example.com",
                    imageDto,
                    List.of(roleDto),
                    LocalDateTime.now()
            );
        }

        static SignUpDto defaultSignUpDto() {
            return new SignUpDto(
                    "Test User",
                    "test@example.com",
                    "password123"
            );
        }

        static UserUpdateDto defaultUserUpdateDto() {
            return new UserUpdateDto(
                    1L,
                    "oldPassword123",
                    "newPassword123"
            );
        }
    }
}