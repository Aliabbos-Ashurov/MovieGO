package com.abbos.moviego.service;

import com.abbos.moviego.dto.CinemaHallCreateDto;
import com.abbos.moviego.dto.CinemaHallResponseDto;
import com.abbos.moviego.dto.CinemaHallUpdateDto;
import com.abbos.moviego.entity.CinemaHall;
import com.abbos.moviego.enums.CinemaHallStatus;
import com.abbos.moviego.service.base.CrudService;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
public interface CinemaHallService extends CrudService<Long, CinemaHall, CinemaHallResponseDto, CinemaHallCreateDto, CinemaHallUpdateDto> {
    List<CinemaHallResponseDto> findAllByStatusIs(CinemaHallStatus status);
}
