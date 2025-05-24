package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.CategoryCreateDto;
import com.abbos.moviego.dto.CategoryResponseDto;
import com.abbos.moviego.dto.CategoryUpdateDto;
import com.abbos.moviego.entity.Category;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.CategoryMapper;
import com.abbos.moviego.repository.CategoryRepository;
import com.abbos.moviego.service.CategoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-24
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @InjectMocks
    CategoryServiceImpl categoryService;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryService self;

    private Category category;
    private CategoryResponseDto responseDto;
    private CategoryCreateDto createDto;
    private CategoryUpdateDto updateDto;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1L)
                .name("Test Name")
                .description("Test Description")
                .build();
        responseDto = new CategoryResponseDto(1L, "Test Name", "Test Description");
        createDto = new CategoryCreateDto("Test Name", "Test Description");
        updateDto = new CategoryUpdateDto(1L, "Test Name", "Test Description");
    }

    @Test
    @DisplayName("Create with valid dto")
    void create_ShouldSaveCategoryWhenValidDtoProvided() {
        when(categoryMapper.fromCreate(createDto)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        categoryService.create(createDto);

        verify(categoryMapper, times(1)).fromCreate(createDto);
        verify(categoryRepository, times(1)).save(any(Category.class));

        verifyNoInteractions(self);
    }

    @Test
    @DisplayName("Update with valid dto")
    void update_ShouldUpdateCategoryWhenValidDtoProvided() {
        when(self.findEntity(1L)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        categoryService.update(updateDto);

        verify(self, times(1)).findEntity(1L);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    @DisplayName("Delete with valid ID should delete category")
    void delete_ShouldDeleteCategoryWhenValidIdProvided() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        categoryService.delete(1L);

        verify(categoryRepository, times(1)).existsById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
        verifyNoInteractions(self, categoryMapper);
    }

    @Test
    @DisplayName("Delete with null ID should throw IllegalArgumentException")
    void delete_ShouldThrowExceptionWhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.delete(null));
        verifyNoInteractions(categoryRepository, self, categoryMapper);
    }

    @Test
    @DisplayName("Delete with non-existent ID should throw ResourceNotFoundException")
    void delete_ShouldThrowExceptionWhenIdDoesNotExist() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> categoryService.delete(1L));

        verify(categoryRepository, times(1)).existsById(1L);
        verifyNoMoreInteractions(categoryRepository);
        verifyNoInteractions(self, categoryMapper);
    }

    @Test
    @DisplayName("Exists with valid ID should return true")
    void exists_ShouldReturnTrueWhenCategoryExists() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        boolean result = categoryService.exists(1L);

        assertTrue(result);
        verify(categoryRepository, times(1)).existsById(1L);
        verifyNoInteractions(self, categoryMapper);
    }

    @Test
    @DisplayName("Exists with non-existent ID should return false")
    void exists_ShouldReturnFalseWhenCategoryDoesNotExist() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        boolean result = categoryService.exists(1L);

        assertFalse(result);
        verify(categoryRepository, times(1)).existsById(1L);
        verifyNoInteractions(self, categoryMapper);
    }

    @Test
    @DisplayName("Find with valid ID should return CategoryResponseDto")
    void find_ShouldReturnCategoryResponseDtoWhenIdIsValid() {
        when(self.findEntity(1L)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.find(1L);

        assertEquals(responseDto, result);
        verify(self, times(1)).findEntity(1L);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoInteractions(categoryRepository);
    }

    @Test
    @DisplayName("FindEntity with valid ID should return Category")
    void findEntity_ShouldReturnCategoryWhenIdIsValid() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.findEntity(1L);

        assertEquals(category, result);
        verify(categoryRepository, times(1)).findById(1L);
        verifyNoInteractions(self, categoryMapper);
    }

    @Test
    @DisplayName("FindEntity with non-existent ID should throw ResourceNotFoundException")
    void findEntity_ShouldThrowExceptionWhenIdDoesNotExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.findEntity(1L));

        verify(categoryRepository, times(1)).findById(1L);
        verifyNoInteractions(self, categoryMapper);
    }

    @Test
    @DisplayName("FindAll should return list of CategoryResponseDto")
    void findAll_ShouldReturnListOfCategoryResponseDto() {
        List<Category> categories = List.of(category);
        List<CategoryResponseDto> responseDtos = List.of(responseDto);
        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toDtoList(categories)).thenReturn(responseDtos);

        List<CategoryResponseDto> result = categoryService.findAll();

        assertEquals(responseDtos, result);
        verify(categoryRepository, times(1)).findAll();
        verify(categoryMapper, times(1)).toDtoList(categories);
        verifyNoInteractions(self);
    }

    @AfterEach
    void tearDown() {
        category = null;
        responseDto = null;
        createDto = null;
        updateDto = null;
    }
}
