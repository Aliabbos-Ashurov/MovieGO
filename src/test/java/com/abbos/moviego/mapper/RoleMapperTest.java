package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.PermissionResponseDto;
import com.abbos.moviego.dto.RoleCreateDto;
import com.abbos.moviego.dto.RoleResponseDto;
import com.abbos.moviego.dto.RoleUpdateDto;
import com.abbos.moviego.entity.Permission;
import com.abbos.moviego.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { RoleMapperImpl.class, PermissionMapperImpl.class })
class RoleMapperTest {

    @Autowired
    private RoleMapper roleMapper;

    @Test
    void testToDto() {
        Role entity = RoleBuilder.defaultRole();

        RoleResponseDto dto = roleMapper.toDto(entity);

        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getName(), dto.name());
        assertEquals(entity.getPermissions().size(), dto.permissions().size());
        Permission permission = entity.getPermissions().iterator().next();
        PermissionResponseDto permissionDto = dto.permissions().iterator().next();
        assertEquals(permission.getId(), permissionDto.id());
        assertEquals(permission.getName(), permissionDto.name());
        assertNull(roleMapper.toDto(null));
    }

    @Test
    void testToEntity() {
        assertNull(roleMapper.toEntity(null));
    }

    @Test
    void testFromCreate() {
        RoleCreateDto dto = DtoBuilder.defaultCreateDto();

        Role entity = roleMapper.fromCreate(dto);

        assertNull(entity.getId());
        assertEquals(dto.name(), entity.getName());
        assertTrue(entity.getPermissions().isEmpty(), "Permissions should be ignored per mapping");
        assertNull(entity.getCreatedAt(), "createdAt should be null as it is set by Auditable's @PrePersist");
        assertNull(roleMapper.fromCreate(null));
    }

    @Test
    void testFromUpdate() {
        RoleUpdateDto dto = DtoBuilder.defaultUpdateDto();

        Role entity = roleMapper.fromUpdate(dto);

        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.name(), entity.getName());
        assertTrue(entity.getPermissions().isEmpty(), "Permissions should be empty as not mapped in update");
        assertNull(entity.getCreatedAt(), "createdAt should be null as it is set by Auditable's @PrePersist");
        assertNull(roleMapper.fromUpdate(null));
    }

    @Test
    void testMergeUpdate() {
        RoleUpdateDto dto = DtoBuilder.defaultUpdateDto();
        Role target = RoleBuilder.defaultRole();

        Role merged = roleMapper.mergeUpdate(target, dto);

        assertEquals(target.getId(), merged.getId());
        assertEquals(dto.name(), merged.getName());
        assertEquals(target.getPermissions(), merged.getPermissions());
        assertEquals(target.getCreatedAt(), merged.getCreatedAt());
        assertEquals(target, roleMapper.mergeUpdate(target, null));
    }

    @Test
    void testToDtoList() {
        List<Role> entities = List.of(RoleBuilder.defaultRole());

        List<RoleResponseDto> dtos = roleMapper.toDtoList(entities);

        assertEquals(1, dtos.size());
        RoleResponseDto dto = dtos.getFirst();
        Role entity = entities.getFirst();
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getName(), dto.name());
        assertEquals(entity.getPermissions().size(), dto.permissions().size());
        Permission permission = entity.getPermissions().iterator().next();
        PermissionResponseDto permissionDto = dto.permissions().iterator().next();
        assertEquals(permission.getId(), permissionDto.id());
        assertEquals(permission.getName(), permissionDto.name());
        assertNull(roleMapper.toDtoList(null));
    }

    @Test
    void testToEntityList() {
        List<RoleResponseDto> dtos = List.of(DtoBuilder.defaultResponseDto());

        List<Role> entities = roleMapper.toEntityList(dtos);

        assertEquals(1, entities.size());
        Role entity = entities.getFirst();
        RoleResponseDto dto = dtos.getFirst();
        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.name(), entity.getName());
        assertEquals(dto.permissions().size(), entity.getPermissions().size());
        PermissionResponseDto permissionDto = dto.permissions().iterator().next();
        Permission permission = entity.getPermissions().iterator().next();
        assertEquals(permissionDto.id(), permission.getId());
        assertEquals(permissionDto.name(), permission.getName());
        assertNull(entity.getCreatedAt(), "createdAt should be null as it is set by Auditable's @PrePersist");
        assertNull(roleMapper.toEntityList(null));
    }

    static class RoleBuilder {
        static Role defaultRole() {
            Permission permission = Permission.builder()
                    .id(1L)
                    .name("VIEW_MOVIE")
                    .build();
            permission.setCreatedAt(LocalDateTime.now());

            return Role.builder()
                    .id(1L)
                    .name("USER")
                    .permissions(new HashSet<>(Set.of(permission)))
                    .build();
        }
    }

    static class DtoBuilder {
        static RoleCreateDto defaultCreateDto() {
            return new RoleCreateDto("USER", Set.of(1L));
        }

        static RoleUpdateDto defaultUpdateDto() {
            return new RoleUpdateDto(1L, "ADMIN");
        }

        static RoleResponseDto defaultResponseDto() {
            PermissionResponseDto permissionDto = new PermissionResponseDto(1L, "VIEW_MOVIE");
            return new RoleResponseDto(1L, "USER", Set.of(permissionDto));
        }
    }
}
