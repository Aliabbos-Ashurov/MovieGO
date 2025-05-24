package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.PermissionResponseDto;
import com.abbos.moviego.dto.RoleCreateDto;
import com.abbos.moviego.dto.RoleResponseDto;
import com.abbos.moviego.dto.RoleUpdateDto;
import com.abbos.moviego.entity.Permission;
import com.abbos.moviego.entity.Role;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.RoleMapper;
import com.abbos.moviego.repository.RoleRepository;
import com.abbos.moviego.service.PermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

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
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;
    private Permission permission;
    private RoleCreateDto roleCreateDto;
    private RoleUpdateDto roleUpdateDto;
    private RoleResponseDto roleResponseDto;

    @BeforeEach
    void setUp() {
        permission = Permission.builder()
                .id(1L)
                .name("VIEW_MOVIES")
                .build();

        role = Role.builder()
                .id(1L)
                .name("USER")
                .permissions(new HashSet<>(Set.of(permission)))
                .build();

        roleCreateDto = new RoleCreateDto(
                "USER",
                Set.of(1L)
        );

        roleUpdateDto = new RoleUpdateDto(
                1L,
                "UPDATED_USER"
        );

        roleResponseDto = new RoleResponseDto(
                1L,
                "USER",
                Set.of(new PermissionResponseDto(1L, "VIEW_MOVIES"))
        );
    }

    @Test
    @DisplayName("Create role with valid RoleCreateDto and permissions")
    void create_shouldSaveRoleWhenValidDtoAndPermissionsProvided() {
        when(permissionService.findAllByIds(Set.of(1L))).thenReturn(Set.of(permission));
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        roleService.create(roleCreateDto);

        verify(permissionService, times(1)).findAllByIds(Set.of(1L));
        verify(roleRepository, times(1)).save(any(Role.class));
        verifyNoInteractions(roleMapper);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when some permissions do not exist")
    void create_shouldThrowExceptionWhenPermissionsNotFound() {
        when(permissionService.findAllByIds(Set.of(1L))).thenReturn(Collections.emptySet());

        assertThrows(ResourceNotFoundException.class, () -> roleService.create(roleCreateDto));

        verify(permissionService, times(1)).findAllByIds(Set.of(1L));
        verify(roleRepository, never()).save(any(Role.class));
        verifyNoInteractions(roleMapper);
    }

    @Test
    @DisplayName("Update role name for existing role")
    void update_shouldUpdateRoleNameWhenRoleExists() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        roleService.update(roleUpdateDto);

        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).save(role);
        verifyNoInteractions(permissionService, roleMapper);
        assertEquals("UPDATED_USER", role.getName());
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when updating non-existent role")
    void update_shouldThrowNotFoundWhenRoleDoesNotExist() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.update(roleUpdateDto));

        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, never()).save(any(Role.class));
        verifyNoInteractions(permissionService, roleMapper);
    }

    @Test
    @DisplayName("Find role by name when it exists")
    void findByName_shouldReturnRoleWhenNameExists() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));

        Role result = roleService.findByName("USER");

        verify(roleRepository, times(1)).findByName("USER");
        assertEquals(role, result);
        verifyNoInteractions(permissionService, roleMapper);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when role name does not exist")
    void findByName_shouldThrowNotFoundWhenNameDoesNotExist() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.findByName("USER"));

        verify(roleRepository, times(1)).findByName("USER");
        verifyNoInteractions(permissionService, roleMapper);
    }

    @Test
    @DisplayName("Find roles by IDs")
    void findAllByIdIn_shouldReturnRolesForGivenIds() {
        Set<Long> ids = Set.of(1L);
        when(roleRepository.findAllByIdIn(ids)).thenReturn(Set.of(role));

        Set<Role> result = roleService.findAllByIdIn(ids);

        verify(roleRepository, times(1)).findAllByIdIn(ids);
        assertEquals(Set.of(role), result);
        verifyNoInteractions(permissionService, roleMapper);
    }

    @Test
    @DisplayName("Delete role when it exists")
    void delete_shouldDeleteRoleWhenExists() {
        when(roleRepository.existsById(1L)).thenReturn(true);

        roleService.delete(1L);

        verify(roleRepository, times(1)).existsById(1L);
        verify(roleRepository, times(1)).deleteById(1L);
        verifyNoInteractions(permissionService, roleMapper);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when deleting non-existent role")
    void delete_shouldThrowNotFoundWhenRoleDoesNotExist() {
        when(roleRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> roleService.delete(1L));

        verify(roleRepository, times(1)).existsById(1L);
        verify(roleRepository, never()).deleteById(anyLong());
        verifyNoInteractions(permissionService, roleMapper);
    }

    @Test
    @DisplayName("Return true when role exists by ID")
    void exists_shouldReturnTrueWhenRoleExists() {
        when(roleRepository.existsById(1L)).thenReturn(true);

        boolean result = roleService.exists(1L);

        verify(roleRepository, times(1)).existsById(1L);
        assertTrue(result);
        verifyNoInteractions(permissionService, roleMapper);
    }

    @Test
    @DisplayName("Return false when role does not exist by ID")
    void exists_shouldReturnFalseWhenRoleDoesNotExist() {
        when(roleRepository.existsById(1L)).thenReturn(false);

        boolean result = roleService.exists(1L);

        verify(roleRepository, times(1)).existsById(1L);
        assertFalse(result);
        verifyNoInteractions(permissionService, roleMapper);
    }

    @Test
    @DisplayName("Find and return RoleResponseDto for existing role")
    void find_shouldReturnRoleDtoWhenRoleExists() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleMapper.toDto(role)).thenReturn(roleResponseDto);

        RoleResponseDto result = roleService.find(1L);

        verify(roleRepository, times(1)).findById(1L);
        verify(roleMapper, times(1)).toDto(role);
        assertEquals(roleResponseDto, result);
        verifyNoInteractions(permissionService);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when finding non-existent role")
    void find_shouldThrowNotFoundWhenRoleDoesNotExist() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.find(1L));

        verify(roleRepository, times(1)).findById(1L);
        verify(roleMapper, never()).toDto(any(Role.class));
        verifyNoInteractions(permissionService);
    }

    @Test
    @DisplayName("Find and return Role entity for existing role")
    void findEntity_shouldReturnRoleWhenExists() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        Role result = roleService.findEntity(1L);

        verify(roleRepository, times(1)).findById(1L);
        assertEquals(role, result);
        verifyNoInteractions(permissionService, roleMapper);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when finding non-existent role entity")
    void findEntity_shouldThrowNotFoundWhenRoleDoesNotExist() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.findEntity(1L));

        verify(roleRepository, times(1)).findById(1L);
        verifyNoInteractions(permissionService, roleMapper);
    }

    @Test
    @DisplayName("Return list of all RoleResponseDto sorted by ID descending")
    void findAll_shouldReturnListOfRoleDtos() {
        List<Role> roles = List.of(role);
        List<RoleResponseDto> dtos = List.of(roleResponseDto);
        when(roleRepository.findAll(any(Sort.class))).thenReturn(roles);
        when(roleMapper.toDtoList(roles)).thenReturn(dtos);

        List<RoleResponseDto> result = roleService.findAll();

        verify(roleRepository, times(1)).findAll(Sort.by(Sort.Direction.DESC, "id"));
        verify(roleMapper, times(1)).toDtoList(roles);
        assertEquals(dtos, result);
        verifyNoInteractions(permissionService);
    }
}
