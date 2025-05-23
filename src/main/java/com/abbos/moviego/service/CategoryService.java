package com.abbos.moviego.service;

import com.abbos.moviego.dto.CategoryCreateDto;
import com.abbos.moviego.dto.CategoryResponseDto;
import com.abbos.moviego.dto.CategoryUpdateDto;
import com.abbos.moviego.entity.Category;
import com.abbos.moviego.service.base.CrudService;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
public interface CategoryService extends CrudService<Long, Category, CategoryResponseDto, CategoryCreateDto, CategoryUpdateDto> {
}
