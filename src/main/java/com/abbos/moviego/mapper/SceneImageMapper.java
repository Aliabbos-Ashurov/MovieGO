package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.SceneImageResponseDto;
import com.abbos.moviego.entity.SceneImage;
import org.mapstruct.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MovieMapper.class, ImageMapper.class})
public interface SceneImageMapper
        extends EntityConverter<SceneImage, SceneImageResponseDto>, CollectionMapper<SceneImage, SceneImageResponseDto> {

    @Override
    @Mapping(target = "movieId", source = "movie.id")
    SceneImageResponseDto toDto(SceneImage sceneImage);


    @Override
    @InheritInverseConfiguration
        // < reverse same logic from toDto >
    SceneImage toEntity(SceneImageResponseDto dto);
}
