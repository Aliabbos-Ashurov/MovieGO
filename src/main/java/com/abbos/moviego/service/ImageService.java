package com.abbos.moviego.service;

import com.abbos.moviego.dto.ImageResponseDto;
import com.abbos.moviego.entity.Image;
import com.abbos.moviego.service.base.SearchService;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
public interface ImageService extends SearchService<Long, Image, ImageResponseDto> {

    Image create(String key, MultipartFile file);

    Image uploadAndBuildOnly(String key, MultipartFile file);

    byte[] download(String key);

    void delete(Long id, String key);

}
