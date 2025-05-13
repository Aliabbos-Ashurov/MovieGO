package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.ImageResponseDto;
import com.abbos.moviego.entity.Image;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.ImageMapper;
import com.abbos.moviego.repository.ImageRepository;
import com.abbos.moviego.service.ImageService;
import com.abbos.moviego.service.aws.S3StorageService;
import com.abbos.moviego.service.base.AbstractService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-07
 */
@Service
public class ImageServiceImpl extends AbstractService<ImageRepository, ImageMapper> implements ImageService {

    private final S3StorageService s3StorageService;

    public ImageServiceImpl(ImageRepository repository,
                            ImageMapper imageMapper,
                            S3StorageService s3StorageService) {
        super(repository, imageMapper);
        this.s3StorageService = s3StorageService;
    }

    @Transactional
    @Override
    public Image create(String key, MultipartFile file) {
        try {
            String generatedName = s3StorageService.uploadObject(key, file);

            Image image = Image.builder()
                    .generatedName(generatedName)
                    .fileName(file.getOriginalFilename())
                    .extension(FilenameUtils.getExtension(file.getOriginalFilename()))
                    .size(file.getSize())
                    .link(s3StorageService.BASE_LINK + "/" + generatedName)
                    .build();

            return repository.save(image);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create image", e);
        }
    }

    @Override
    public byte[] download(String key) {
        return s3StorageService.downloadObject(key);
    }

    @Transactional
    @Override
    public void delete(Long id, String key) {
        if (!exists(id)) {
            throwNotFound(id);
        }
        s3StorageService.deleteObject(key);
        repository.deleteById(id);
    }

    @Override
    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    @Override
    public ImageResponseDto find(Long id) {
        return mapper.toDto(findEntity(id));
    }

    @Override
    public Image findEntity(Long id) {
        return repository.findById(id).orElseThrow(
                () -> returnNotFound(id)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<ImageResponseDto> findAll() {
        return mapper.toDtoList(
                repository.findAll()
        );
    }

    private void throwNotFound(Long id) {
        throw returnNotFound(id);
    }

    private ResourceNotFoundException returnNotFound(Long id) {
        return new ResourceNotFoundException("Image not found with id: " + id);
    }

}
