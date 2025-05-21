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
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.abbos.moviego.util.CacheKeys.CategoryKeys.CATEGORIES;
import static com.abbos.moviego.util.CacheKeys.CategoryKeys.CATEGORY;
import static com.abbos.moviego.util.CacheKeys.FIND_ALL;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-07
 */
@Service
public class CategoryServiceImpl extends AbstractService<CategoryRepository, CategoryMapper> implements CategoryService {

    private final CategoryService self;

    public CategoryServiceImpl(CategoryRepository repository,
                               CategoryMapper mapper,
                               @Lazy CategoryService categoryService) {
        super(repository, mapper);
        this.self = categoryService;
    }

    @CacheEvict(value = CATEGORIES, key = FIND_ALL)
    @Override
    public void create(CategoryCreateDto dto) {
        Category category = mapper.fromCreate(dto);
        repository.save(category);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CATEGORY, key = "#dto.id()"),
            @CacheEvict(value = CATEGORIES, key = FIND_ALL)
    })
    @Override
    public void update(CategoryUpdateDto dto) {
        Category category = self.findEntity(dto.id());
        category.setName(dto.name());
        category.setDescription(dto.description());
        repository.save(category);
    }

    @Caching(evict = {
            @CacheEvict(value = CATEGORY, key = "#id"),
            @CacheEvict(value = CATEGORIES, key = FIND_ALL)
    })
    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        if (!exists(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    @Override
    public CategoryResponseDto find(Long id) {
        Category category = self.findEntity(id);
        return mapper.toDto(category);
    }


    @Cacheable(value = CATEGORY, key = "#id")
    @Override
    public Category findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Cacheable(value = CATEGORIES, key = FIND_ALL)
    @Override
    public List<CategoryResponseDto> findAll() {
        return mapper.toDtoList(repository.findAll());
    }
}