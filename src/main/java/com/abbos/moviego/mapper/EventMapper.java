package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.EventCreateDto;
import com.abbos.moviego.dto.EventResponseDto;
import com.abbos.moviego.dto.EventUpdateDto;
import com.abbos.moviego.entity.Event;
import org.mapstruct.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MovieMapper.class, CinemaHallMapper.class, ImageMapper.class, UtilMapper.class})
public interface EventMapper
        extends EntityMapper<Event, EventResponseDto, EventCreateDto, EventUpdateDto> {

    @Override
    @Mappings({
            @Mapping(target = "price", source = "price", qualifiedByName = "bigDecimalToFormattedString"),
            @Mapping(target = "status", source = "status", qualifiedByName = "enumToString"),
    })
    EventResponseDto toDto(Event event);


    @Override
    @Mapping(target = "cinemaHall", source = "cinemaHallId", ignore = true)
    @Mapping(target = "movie", source = "movieId", ignore = true)
    Event fromCreate(EventCreateDto dto);
}
