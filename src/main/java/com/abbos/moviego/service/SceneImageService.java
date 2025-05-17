package com.abbos.moviego.service;

import com.abbos.moviego.dto.SceneImageResponseDto;
import com.abbos.moviego.entity.Movie;
import com.abbos.moviego.entity.SceneImage;
import com.abbos.moviego.service.base.DeleteService;
import com.abbos.moviego.service.base.SearchService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
public interface SceneImageService extends SearchService<Long, SceneImage, SceneImageResponseDto>, DeleteService<Long> {


    List<SceneImage> create(Movie movie, List<MultipartFile> multipartFile);

    List<String> findSceneImagesLink(Long movieId);
}
