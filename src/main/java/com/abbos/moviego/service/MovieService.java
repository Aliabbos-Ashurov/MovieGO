package com.abbos.moviego.service;

import com.abbos.moviego.dto.MovieCreateDto;
import com.abbos.moviego.dto.MovieResponseDto;
import com.abbos.moviego.dto.MovieUpdateDto;
import com.abbos.moviego.entity.Movie;
import com.abbos.moviego.service.base.CrudService;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
public interface MovieService extends CrudService<Long, Movie, MovieResponseDto, MovieCreateDto, MovieUpdateDto> {

    List<MovieResponseDto> findByTitleLike(String title);
}
