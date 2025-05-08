package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.CategoryCreateDto;
import com.abbos.moviego.dto.CategoryResponseDto;
import com.abbos.moviego.dto.CategoryUpdateDto;
import com.abbos.moviego.entity.Category;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.CategoryMapper;
import com.abbos.moviego.repository.CategoryRepository;
import com.abbos.moviego.service.CategoryService;
import com.abbos.moviego.service.base.AbstractService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-07
 */
@Service
public class CategoryServiceImpl extends AbstractService<CategoryRepository, CategoryMapper> implements CategoryService {

    public CategoryServiceImpl(CategoryRepository repository, CategoryMapper categoryMapper) {
        super(repository, categoryMapper);
    }

    @Override
    public CategoryResponseDto create(CategoryCreateDto dto) {
        Category category = mapper.fromCreate(dto);
        Category saved = repository.save(category);
        return mapper.toDto(saved);
    }

    @Override
    public CategoryResponseDto update(CategoryUpdateDto dto) {
        Category category = findEntity(dto.id());
        category.setName(dto.name());
        category.setDescription(dto.description());
        Category saved = repository.save(category);
        return mapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!exists(id)) {
            throwNotFound(id);
        }
        repository.deleteById(id);
    }

    @Override
    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    @Override
    public CategoryResponseDto find(Long id) {
        return mapper.toDto(findEntity(id));
    }

    @Override
    public Category findEntity(Long id) {
        return repository.findById(id).orElseThrow(
                () -> returnNotFound(id)
        );
    }

    @Override
    public List<CategoryResponseDto> findAll() {
        return mapper.toDtoList(
                repository.findAll()
        );
    }

    private ResourceNotFoundException returnNotFound(Long id) {
        return new ResourceNotFoundException("Category not found with id: {}", id);
    }

    private void throwNotFound(Long id) {
        throw returnNotFound(id);
    }
}
