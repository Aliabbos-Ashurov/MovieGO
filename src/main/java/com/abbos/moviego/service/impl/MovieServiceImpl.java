package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.MovieCreateDto;
import com.abbos.moviego.dto.MovieResponseDto;
import com.abbos.moviego.dto.MovieUpdateDto;
import com.abbos.moviego.entity.Category;
import com.abbos.moviego.entity.Image;
import com.abbos.moviego.entity.Movie;
import com.abbos.moviego.entity.SceneImage;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.MovieMapper;
import com.abbos.moviego.repository.MovieRepository;
import com.abbos.moviego.service.CategoryService;
import com.abbos.moviego.service.ImageService;
import com.abbos.moviego.service.MovieService;
import com.abbos.moviego.service.SceneImageService;
import com.abbos.moviego.service.base.AbstractService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-07
 */
@Service
public class MovieServiceImpl extends AbstractService<MovieRepository, MovieMapper> implements MovieService {

    private final CategoryService categoryService;
    private final ImageService imageService;
    private final SceneImageService sceneImageService;

    public MovieServiceImpl(MovieRepository repository,
                            MovieMapper movieMapper,
                            CategoryService categoryService,
                            ImageService imageService,
                            SceneImageService sceneImageService) {
        super(repository, movieMapper);
        this.categoryService = categoryService;
        this.imageService = imageService;
        this.sceneImageService = sceneImageService;
    }

    @Transactional
    @Override
    public MovieResponseDto create(MovieCreateDto dto) {
        Category category = categoryService.findEntity(dto.categoryId());

        Image posterImage = imageService.create(
                FilenameUtils.getBaseName(dto.posterImage().getOriginalFilename()),
                dto.posterImage()
        );

        // Create movie without scene images first
        Movie movie = Movie.builder()
                .title(dto.title())
                .durationMinutes(dto.durationMinutes())
                .language(dto.language())
                .rating(dto.rating())
                .trailerLink(dto.trailerLink())
                .description(dto.description())
                .category(category)
                .posterImage(posterImage)
                .sceneImages(Collections.emptyList()) // Initialize empty list
                .build();

        // Save movie to generate ID
        Movie savedMovie = repository.save(movie);

        if (dto.sceneImages() != null && !dto.sceneImages().isEmpty()) {
            List<SceneImage> sceneImages = sceneImageService.create(savedMovie, dto.sceneImages());
            savedMovie.setSceneImages(sceneImages);
        }
        return mapper.toDto(savedMovie);
    }

    @Override
    public MovieResponseDto update(MovieUpdateDto dto) {
        Movie movie = mapper.fromUpdate(dto);
        Movie saved = repository.save(movie);
        return mapper.toDto(saved);
    }

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

    @Override
    public MovieResponseDto find(Long id) {
        return mapper.toDto(findEntity(id));
    }

    @Override
    public Movie findEntity(Long id) {
        return repository.findById(id).orElseThrow(
                () -> returnNotFound(id)
        );
    }

    @Override
    public List<MovieResponseDto> findAll() {
        return mapper.toDtoList(
                repository.findAll()
        );
    }


    @Override
    public List<MovieResponseDto> findByTitleLike(String title) {
        return mapper.toDtoList(
                repository.findByTitleLike(title)
        );
    }

    private void throwNotFound(Long id) {
        throw returnNotFound(id);
    }

    private ResourceNotFoundException returnNotFound(Long id) {
        return new ResourceNotFoundException("Movie not found with id: " + id);
    }
}
