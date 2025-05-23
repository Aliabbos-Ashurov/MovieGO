package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.PermissionCreateDto;
import com.abbos.moviego.dto.PermissionResponseDto;
import com.abbos.moviego.dto.PermissionUpdateDto;
import com.abbos.moviego.entity.Permission;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { PermissionMapperImpl.class })
class PermissionMapperTest {

    @Autowired
    private PermissionMapper permissionMapper;

    @Test
    void testToDto() {
        Permission entity = PermissionBuilder.defaultPermission();

        PermissionResponseDto dto = permissionMapper.toDto(entity);

        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getName(), dto.name());
        assertNull(permissionMapper.toDto(null));
    }

    @Test
    void testFromCreate() {
        PermissionCreateDto dto = DtoBuilder.defaultCreateDto();

        Permission entity = permissionMapper.fromCreate(dto);

        assertNull(entity.getId());
        assertEquals(dto.name(), entity.getName());
        assertNull(entity.getCreatedAt(), "createdAt should be null as it is set by Auditable's @PrePersist");
        assertNull(permissionMapper.fromCreate(null));
    }

    @Test
    void testFromUpdate() {
        PermissionUpdateDto dto = DtoBuilder.defaultUpdateDto();

        Permission entity = permissionMapper.fromUpdate(dto);

        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.name(), entity.getName());
        assertNull(entity.getCreatedAt(), "createdAt should be null as it is set by Auditable's @PrePersist");
        assertNull(permissionMapper.fromUpdate(null));
    }

    @Test
    void testMergeUpdate() {
        PermissionUpdateDto dto = DtoBuilder.defaultUpdateDto();
        Permission target = PermissionBuilder.defaultPermission();

        Permission merged = permissionMapper.mergeUpdate(target, dto);

        assertEquals(target.getId(), merged.getId());
        assertEquals(dto.name(), merged.getName());
        assertEquals(target.getCreatedAt(), merged.getCreatedAt());
        assertEquals(target, permissionMapper.mergeUpdate(target, null));
    }

    @Test
    void testToDtoList() {
        List<Permission> entities = List.of(PermissionBuilder.defaultPermission());

        List<PermissionResponseDto> dtos = permissionMapper.toDtoList(entities);

        assertEquals(1, dtos.size());
        PermissionResponseDto dto = dtos.getFirst();
        Permission entity = entities.getFirst();
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getName(), dto.name());
        assertNull(permissionMapper.toDtoList(null));
    }

    @Test
    void testToEntityList() {
        List<PermissionResponseDto> dtos = List.of(DtoBuilder.defaultResponseDto());

        List<Permission> entities = permissionMapper.toEntityList(dtos);

        assertEquals(1, entities.size());
        Permission entity = entities.getFirst();
        PermissionResponseDto dto = dtos.getFirst();
        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.name(), entity.getName());
        assertNull(entity.getCreatedAt(), "createdAt should be null as it is set by Auditable's @PrePersist");
        assertNull(permissionMapper.toEntityList(null));
    }

    static class PermissionBuilder {
        static Permission defaultPermission() {
            return Permission.builder()
                    .id(1L)
                    .name("VIEW_MOVIE")
                    .build();
        }
    }

    static class DtoBuilder {
        static PermissionCreateDto defaultCreateDto() {
            return new PermissionCreateDto("VIEW_MOVIE");
        }

        static PermissionUpdateDto defaultUpdateDto() {
            return new PermissionUpdateDto(1L, "EDIT_MOVIE");
        }

        static PermissionResponseDto defaultResponseDto() {
            return new PermissionResponseDto(1L, "VIEW_MOVIE");
        }
    }
}