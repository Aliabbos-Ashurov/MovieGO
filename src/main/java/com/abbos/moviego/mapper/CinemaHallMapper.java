package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.CinemaHallCreateDto;
import com.abbos.moviego.dto.CinemaHallResponseDto;
import com.abbos.moviego.dto.CinemaHallUpdateDto;
import com.abbos.moviego.entity.CinemaHall;
import com.abbos.moviego.mapper.base.EntityMapper;
import com.abbos.moviego.mapper.base.UtilMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ImageMapper.class, UtilMapper.class})
public interface CinemaHallMapper
        extends EntityMapper<CinemaHall, CinemaHallResponseDto, CinemaHallCreateDto, CinemaHallUpdateDto> {

    @Override
    @Mapping(target = "status", source = "status", qualifiedByName = "enumToString")
    CinemaHallResponseDto toDto(CinemaHall entity);

    @Override
    @Mapping(target = "image", source = "image", ignore = true)
    CinemaHall fromCreate(CinemaHallCreateDto dto);

}
