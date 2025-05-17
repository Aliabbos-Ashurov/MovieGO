package com.abbos.moviego.service;

import com.abbos.moviego.dto.MovieCreateDto;
import com.abbos.moviego.dto.MovieDetailDto;
import com.abbos.moviego.dto.MovieResponseDto;
import com.abbos.moviego.dto.MovieUpdateDto;
import com.abbos.moviego.entity.Movie;
import com.abbos.moviego.service.base.CrudService;

import java.util.List;
import java.util.Map;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
public interface MovieService extends CrudService<Long, Movie, MovieResponseDto, MovieCreateDto, MovieUpdateDto> {

    List<MovieResponseDto> findAllEager();

    Map<String, List<MovieResponseDto>> findAllGroupedByCategory();

    MovieResponseDto findLastMovie();

    MovieDetailDto findMovieDetail(Long movieId);
}
