package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.CategoryCreateDto;
import com.abbos.moviego.dto.CategoryResponseDto;
import com.abbos.moviego.entity.Category;
import com.abbos.moviego.mapper.base.CollectionMapper;
import com.abbos.moviego.mapper.base.CreateMapper;
import com.abbos.moviego.mapper.base.EntityConverter;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper
        extends EntityConverter<Category, CategoryResponseDto>, CreateMapper<Category, CategoryCreateDto>,
        CollectionMapper<Category, CategoryResponseDto> {
}
