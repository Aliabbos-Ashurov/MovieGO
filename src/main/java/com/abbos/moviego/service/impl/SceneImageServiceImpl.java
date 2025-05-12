package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.SceneImageResponseDto;
import com.abbos.moviego.entity.Image;
import com.abbos.moviego.entity.Movie;
import com.abbos.moviego.entity.SceneImage;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.SceneImageMapper;
import com.abbos.moviego.repository.SceneImageRepository;
import com.abbos.moviego.service.ImageService;
import com.abbos.moviego.service.SceneImageService;
import com.abbos.moviego.service.base.AbstractService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-07
 */
@Service
public class SceneImageServiceImpl
        extends AbstractService<SceneImageRepository, SceneImageMapper> implements SceneImageService {

    private final ImageService imageService;

    public SceneImageServiceImpl(SceneImageRepository repository,
                                 ImageService imageService,
                                 SceneImageMapper sceneImageMapper) {
        super(repository, sceneImageMapper);
        this.imageService = imageService;
    }

    @Transactional
    @Override
    public List<SceneImage> create(Movie movie, List<MultipartFile> multipartFiles) {
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            return Collections.emptyList();
        }

        return multipartFiles.stream()
                .map(file -> {
                    Image image = imageService.create(
                            FilenameUtils.getBaseName(file.getOriginalFilename()),
                            file
                    );
                    SceneImage sceneImage = SceneImage.builder()
                            .image(image)
                            .movie(movie)
                            .build();

                    return repository.save(sceneImage);
                })
                .collect(Collectors.toList());
    }

    @Transactional
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
    public SceneImageResponseDto find(Long id) {
        return mapper.toDto(findEntity(id));
    }

    @Transactional(readOnly = true)
    @Override
    public SceneImage findEntity(Long id) {
        return repository.findById(id).orElseThrow(
                () -> returnNotFound(id)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<SceneImageResponseDto> findAll() {
        return mapper.toDtoList(
                repository.findAll()
        );
    }

    @Override
    public List<SceneImageResponseDto> findSceneImagesByMovieId(Long movieId) {
        return mapper.toDtoList(repository.findSceneImagesByMovieId(movieId));
    }

    private void throwNotFound(Long id) {
        throw returnNotFound(id);
    }

    private ResourceNotFoundException returnNotFound(Long id) {
        return new ResourceNotFoundException("Scene Image not found with id: " + id);
    }
}
