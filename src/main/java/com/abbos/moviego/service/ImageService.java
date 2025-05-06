package com.abbos.moviego.service;

import com.abbos.moviego.dto.ImageResponseDto;
import com.abbos.moviego.entity.Image;
import com.abbos.moviego.service.base.SearchService;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
public interface ImageService extends SearchService<Long, ImageResponseDto> {

    List<Image> findImagesByMimeType(String mimeType);
}
