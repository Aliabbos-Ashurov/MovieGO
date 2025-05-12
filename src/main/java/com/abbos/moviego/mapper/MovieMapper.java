package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.MovieCreateDto;
import com.abbos.moviego.dto.MovieResponseDto;
import com.abbos.moviego.dto.MovieUpdateDto;
import com.abbos.moviego.entity.Movie;
import com.abbos.moviego.mapper.base.EntityMapper;
import org.mapstruct.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CategoryMapper.class, ImageMapper.class, SceneImageMapper.class}
)
public interface MovieMapper
        extends EntityMapper<Movie, MovieResponseDto, MovieCreateDto, MovieUpdateDto> {

    @Override
    @Mappings({
            @Mapping(target = "category", source = "categoryId", ignore = true),
            @Mapping(target = "posterImage", source = "posterImage", ignore = true),
            @Mapping(target = "sceneImages", source = "sceneImages", ignore = true)
    })
    Movie fromCreate(MovieCreateDto dto);

}
