package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.MovieCreateDto;
import com.abbos.moviego.dto.MovieDetailDto;
import com.abbos.moviego.dto.MovieResponseDto;
import com.abbos.moviego.dto.MovieUpdateDto;
import com.abbos.moviego.entity.Category;
import com.abbos.moviego.entity.Image;
import com.abbos.moviego.entity.Movie;
import com.abbos.moviego.entity.SceneImage;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.MovieMapper;
import com.abbos.moviego.repository.MovieRepository;
import com.abbos.moviego.service.*;
import com.abbos.moviego.service.base.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.abbos.moviego.util.CacheKeys.FIND_ALL;
import static com.abbos.moviego.util.CacheKeys.MovieKeys.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-07
 */
@Slf4j
@Service
public class MovieServiceImpl extends AbstractService<MovieRepository, MovieMapper> implements MovieService {

    private final CategoryService categoryService;
    private final ImageService imageService;
    private final SceneImageService sceneImageService;
    private final EventService eventService;
    private final MovieService self;

    public MovieServiceImpl(MovieRepository repository,
                            MovieMapper movieMapper,
                            CategoryService categoryService,
                            ImageService imageService,
                            SceneImageService sceneImageService,
                            @Lazy EventService eventService,
                            @Lazy MovieService movieService) {
        super(repository, movieMapper);
        this.categoryService = categoryService;
        this.imageService = imageService;
        this.sceneImageService = sceneImageService;
        this.eventService = eventService;
        this.self = movieService;
    }

    @CacheEvict(value = MOVIES, key = FIND_ALL)
    @Transactional
    @Override
    public void create(MovieCreateDto dto) {
        Category category = categoryService.findEntity(dto.categoryId());

        Image posterImage = imageService.create(
                FilenameUtils.getBaseName(dto.posterImage().getOriginalFilename()),
                dto.posterImage()
        );

        Movie movie = Movie.builder()
                .title(dto.title())
                .durationMinutes(dto.durationMinutes())
                .language(dto.language())
                .rating(dto.rating())
                .trailerLink(dto.trailerLink())
                .description(dto.description())
                .category(category)
                .posterImage(posterImage)
                .sceneImages(Collections.emptyList())
                .build();

        Movie savedMovie = repository.save(movie);

        if (dto.sceneImages() != null && !dto.sceneImages().isEmpty()) {
            List<SceneImage> sceneImages = sceneImageService.create(savedMovie, dto.sceneImages());
            savedMovie.setSceneImages(sceneImages);
        }
    }

    @Caching(evict = {
            @CacheEvict(value = MOVIE, key = "#dto.id()"),
            @CacheEvict(value = MOVIE_DETAIL, key = "#dto.id()"),
            @CacheEvict(value = MOVIES, key = FIND_ALL)
    })
    @Override
    public void update(MovieUpdateDto dto) {
        Movie movie = mapper.fromUpdate(dto);
        repository.save(movie);
    }

    @Caching(evict = {
            @CacheEvict(value = MOVIE, key = "#id"),
            @CacheEvict(value = MOVIE_DETAIL, key = "#id"),
            @CacheEvict(value = MOVIES, key = FIND_ALL)
    })
    @Override
    public void delete(Long id) {
        if (!exists(id)) {
            throwNotFound(id);
        }
        repository.deleteById(id);
    }

    @Override
    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public MovieResponseDto find(Long id) {
        return mapper.toDto(self.findEntity(id));
    }

    @Cacheable(value = MOVIE, key = "#id")
    @Transactional(readOnly = true)
    @Override
    public Movie findEntity(Long id) {
        return repository.findById(id).orElseThrow(
                () -> returnNotFound(id)
        );
    }

    @Cacheable(value = MOVIES, key = FIND_ALL)
    @Transactional(readOnly = true)
    @Override
    public List<MovieResponseDto> findAllEager() {
        return mapper.toDtoList(
                repository.findAllEager()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Map<String, List<MovieResponseDto>> findAllGroupedByCategory() {
        return self.findAllEager()
                .stream()
                .collect(Collectors.groupingBy(m -> m.category().name()));
    }

    @Override
    @Transactional(readOnly = true)
    public MovieDetailDto findLastMovie() {
        var pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"));
        return self.fetchMovieDetail(null, pageable, false);
    }

    @Override
    @Transactional(readOnly = true)
    public MovieDetailDto findMovieDetail(Long movieId) {
        var movie = Objects.requireNonNull(self.fetchMovieDetail(movieId, Pageable.unpaged(), true));
        movie.setEvents(eventService.findSimpleEventDtoByMovieId(movieId));
        movie.setSceneImageLinks(sceneImageService.findSceneImagesLink(movieId));
        return movie;
    }

    @Override
    @Cacheable(value = MOVIE_DETAIL, key = "#id != null ? #id : 'last'")
    public MovieDetailDto fetchMovieDetail(Long id, Pageable pageable, boolean throwEx) {
        var results = repository.findMovieBaseDetails(id, pageable);
        if (results.isEmpty()) {
            if (throwEx) {
                throwNotFound(id);
            } else {
                log.info("::: MOVIE NOT FOUND WITH ID {} :::", id);
                return null;
            }
        }
        return results.getFirst();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MovieResponseDto> findAll() {
        return mapper.toDtoList(
                repository.findAll()
        );
    }

    private void throwNotFound(Long id) {
        throw returnNotFound(id);
    }

    private ResourceNotFoundException returnNotFound(Long id) {
        return new ResourceNotFoundException("Movie not found with id: " + id);
    }
}
