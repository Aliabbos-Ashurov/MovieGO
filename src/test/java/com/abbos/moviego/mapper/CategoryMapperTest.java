package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.CategoryCreateDto;
import com.abbos.moviego.dto.CategoryResponseDto;
import com.abbos.moviego.dto.CategoryUpdateDto;
import com.abbos.moviego.entity.Category;
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
@SpringBootTest(classes = { CategoryMapperImpl.class })
class CategoryMapperTest {

    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    void testToDto() {
        Category category = CategoryBuilder.defaultCategory();

        CategoryResponseDto dto = categoryMapper.toDto(category);

        assertEquals(category.getId(), dto.id());
        assertEquals(category.getName(), dto.name());
        assertEquals(category.getDescription(), dto.description());
        assertNull(categoryMapper.toDto(null));
    }

    @Test
    void testToEntity() {
        CategoryResponseDto dto = DtoBuilder.defaultResponseDto();

        Category entity = categoryMapper.toEntity(dto);

        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.name(), entity.getName());
        assertEquals(dto.description(), entity.getDescription());
        assertNull(categoryMapper.toEntity(null));
    }

    @Test
    void testFromCreate() {
        CategoryCreateDto createDto = DtoBuilder.defaultCreateDto();

        Category entity = categoryMapper.fromCreate(createDto);

        assertNull(entity.getId());
        assertEquals(createDto.name(), entity.getName());
        assertEquals(createDto.description(), entity.getDescription());
        assertNull(categoryMapper.fromCreate(null));
    }

    @Test
    void testFromUpdate() {
        CategoryUpdateDto updateDto = DtoBuilder.defaultUpdateDto();

        Category entity = categoryMapper.fromUpdate(updateDto);

        assertEquals(updateDto.id(), entity.getId());
        assertEquals(updateDto.name(), entity.getName());
        assertEquals(updateDto.description(), entity.getDescription());
        assertNull(categoryMapper.fromUpdate(null));
    }

    @Test
    void testMergeUpdate() {
        CategoryUpdateDto updateDto = DtoBuilder.defaultUpdateDto();
        Category original = CategoryBuilder.defaultCategory();

        Category merged = categoryMapper.mergeUpdate(original, updateDto);

        assertEquals(original.getId(), merged.getId(), "ID should not change");
        assertEquals(updateDto.name(), merged.getName());
        assertEquals(updateDto.description(), merged.getDescription());
        assertEquals(original, categoryMapper.mergeUpdate(original, null));
    }

    @Test
    void testToDtoList() {
        List<Category> entities = List.of(CategoryBuilder.defaultCategory());

        List<CategoryResponseDto> dtos = categoryMapper.toDtoList(entities);

        assertEquals(1, dtos.size());
        assertEquals(entities.getFirst().getId(), dtos.getFirst().id());
        assertNull(categoryMapper.toDtoList(null));
    }

    @Test
    void testToEntityList() {
        List<CategoryResponseDto> dtos = List.of(DtoBuilder.defaultResponseDto());

        List<Category> entities = categoryMapper.toEntityList(dtos);

        assertEquals(1, entities.size());
        assertEquals(dtos.getFirst().id(), entities.getFirst().getId());
        assertNull(categoryMapper.toEntityList(null));
    }

    private static class CategoryBuilder {
        static Category defaultCategory() {
            Category category = new Category();
            category.setId(1L);
            category.setName("Horror");
            category.setDescription("Scary movies type");
            return category;
        }
    }

    private static class DtoBuilder {
        static CategoryResponseDto defaultResponseDto() {
            return new CategoryResponseDto(1L, "Horror", "Scary movies type");
        }

        static CategoryCreateDto defaultCreateDto() {
            return new CategoryCreateDto("Comedy", "Funny movies");
        }

        static CategoryUpdateDto defaultUpdateDto() {
            return new CategoryUpdateDto(1L, "Action", "Action packed movies");
        }
    }
}