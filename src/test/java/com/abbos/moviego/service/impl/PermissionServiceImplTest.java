package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.PermissionCreateDto;
import com.abbos.moviego.dto.PermissionResponseDto;
import com.abbos.moviego.dto.PermissionUpdateDto;
import com.abbos.moviego.entity.Permission;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.PermissionMapper;
import com.abbos.moviego.repository.PermissionRepository;
import com.abbos.moviego.service.PermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith(MockitoExtension.class)
class PermissionServiceImplTest {

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private PermissionMapper permissionMapper;

    @Mock
    private PermissionService self;

    @InjectMocks
    private PermissionServiceImpl permissionService;

    private Permission permission;
    private PermissionCreateDto permissionCreateDto;
    private PermissionUpdateDto permissionUpdateDto;
    private PermissionResponseDto permissionResponseDto;

    @BeforeEach
    void setUp() {
        permission = Permission.builder()
                .id(1L)
                .name("VIEW_MOVIES")
                .build();

        permissionCreateDto = new PermissionCreateDto("VIEW_MOVIES");

        permissionUpdateDto = new PermissionUpdateDto(1L, "EDIT_MOVIES");

        permissionResponseDto = new PermissionResponseDto(1L, "VIEW_MOVIES");
    }

    @Test
    @DisplayName("Create permission with valid PermissionCreateDto")
    void create_shouldSavePermissionWhenValidDtoProvided() {
        when(permissionMapper.fromCreate(permissionCreateDto)).thenReturn(permission);
        when(permissionRepository.save(any(Permission.class))).thenReturn(permission);

        permissionService.create(permissionCreateDto);

        verify(permissionMapper, times(1)).fromCreate(permissionCreateDto);
        verify(permissionRepository, times(1)).save(permission);
        verifyNoInteractions(self);
    }

    @Test
    @DisplayName("Update permission with valid PermissionUpdateDto")
    void update_shouldUpdatePermissionWhenValidDtoProvided() {
        when(permissionMapper.fromUpdate(permissionUpdateDto)).thenReturn(permission);
        when(permissionRepository.save(any(Permission.class))).thenReturn(permission);

        permissionService.update(permissionUpdateDto);

        verify(permissionMapper, times(1)).fromUpdate(permissionUpdateDto);
        verify(permissionRepository, times(1)).save(permission);
        verifyNoInteractions(self);
    }

    @Test
    @DisplayName("Find permissions by IDs")
    void findAllByIds_shouldReturnPermissionsForGivenIds() {
        Set<Long> ids = Set.of(1L);
        when(permissionRepository.findAllByIds(ids)).thenReturn(Set.of(permission));

        Set<Permission> result = permissionService.findAllByIds(ids);

        verify(permissionRepository, times(1)).findAllByIds(ids);
        assertEquals(Set.of(permission), result);
        verifyNoInteractions(permissionMapper, self);
    }

    @Test
    @DisplayName("Delete permission when it exists")
    void delete_shouldDeletePermissionWhenExists() {
        when(permissionRepository.existsById(1L)).thenReturn(true);

        permissionService.delete(1L);

        verify(permissionRepository, times(1)).existsById(1L);
        verify(permissionRepository, times(1)).deleteById(1L);
        verifyNoInteractions(permissionMapper, self);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when deleting non-existent permission")
    void delete_shouldThrowNotFoundWhenPermissionDoesNotExist() {
        when(permissionRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> permissionService.delete(1L));

        verify(permissionRepository, times(1)).existsById(1L);
        verify(permissionRepository, never()).deleteById(anyLong());
        verifyNoInteractions(permissionMapper, self);
    }

    @Test
    @DisplayName("Return true when permission exists by ID")
    void exists_shouldReturnTrueWhenPermissionExists() {
        when(permissionRepository.existsById(1L)).thenReturn(true);

        boolean result = permissionService.exists(1L);

        verify(permissionRepository, times(1)).existsById(1L);
        assertTrue(result);
        verifyNoInteractions(permissionMapper, self);
    }

    @Test
    @DisplayName("Return false when permission does not exist by ID")
    void exists_shouldReturnFalseWhenPermissionDoesNotExist() {
        when(permissionRepository.existsById(1L)).thenReturn(false);

        boolean result = permissionService.exists(1L);

        verify(permissionRepository, times(1)).existsById(1L);
        assertFalse(result);
        verifyNoInteractions(permissionMapper, self);
    }

    @Test
    @DisplayName("Find and return PermissionResponseDto for existing permission")
    void find_shouldReturnPermissionDtoWhenPermissionExists() {
        when(self.findEntity(1L)).thenReturn(permission);
        when(permissionMapper.toDto(permission)).thenReturn(permissionResponseDto);

        PermissionResponseDto result = permissionService.find(1L);

        verify(self, times(1)).findEntity(1L);
        verify(permissionMapper, times(1)).toDto(permission);
        assertEquals(permissionResponseDto, result);
        verifyNoInteractions(permissionRepository);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when finding non-existent permission")
    void find_shouldThrowNotFoundWhenPermissionDoesNotExist() {
        when(self.findEntity(1L)).thenThrow(new ResourceNotFoundException("Permission not found with id: 1"));

        assertThrows(ResourceNotFoundException.class, () -> permissionService.find(1L));

        verify(self, times(1)).findEntity(1L);
        verify(permissionMapper, never()).toDto(any(Permission.class));
        verifyNoInteractions(permissionRepository);
    }

    @Test
    @DisplayName("Find and return Permission entity for existing permission")
    void findEntity_shouldReturnPermissionWhenExists() {
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));

        Permission result = permissionService.findEntity(1L);

        verify(permissionRepository, times(1)).findById(1L);
        assertEquals(permission, result);
        verifyNoInteractions(permissionMapper, self);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when finding non-existent permission entity")
    void findEntity_shouldThrowNotFoundWhenPermissionDoesNotExist() {
        when(permissionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> permissionService.findEntity(1L));

        verify(permissionRepository, times(1)).findById(1L);
        verifyNoInteractions(permissionMapper, self);
    }

    @Test
    @DisplayName("Return list of all PermissionResponseDto sorted by ID descending")
    void findAll_shouldReturnListOfPermissionDtos() {
        List<Permission> permissions = List.of(permission);
        List<PermissionResponseDto> dtos = List.of(permissionResponseDto);
        when(permissionRepository.findAll(any(Sort.class))).thenReturn(permissions);
        when(permissionMapper.toDtoList(permissions)).thenReturn(dtos);

        List<PermissionResponseDto> result = permissionService.findAll();

        verify(permissionRepository, times(1)).findAll(Sort.by(Sort.Direction.DESC, "id"));
        verify(permissionMapper, times(1)).toDtoList(permissions);
        assertEquals(dtos, result);
        verifyNoInteractions(self);
    }
}
