package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.ImageResponseDto;
import com.abbos.moviego.dto.SceneImageResponseDto;
import com.abbos.moviego.entity.Image;
import com.abbos.moviego.entity.Movie;
import com.abbos.moviego.entity.SceneImage;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.SceneImageMapper;
import com.abbos.moviego.repository.SceneImageRepository;
import com.abbos.moviego.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith(MockitoExtension.class)
class SceneImageServiceImplTest {

    @Mock
    private SceneImageRepository sceneImageRepository;

    @Mock
    private SceneImageMapper sceneImageMapper;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private SceneImageServiceImpl sceneImageService;

    private Movie movie;
    private Image image;
    private SceneImage sceneImage;
    private SceneImageResponseDto sceneImageResponseDto;
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        movie = Movie.builder()
                .id(1L)
                .title("Test Movie")
                .durationMinutes(120)
                .language("English")
                .rating("6.6")
                .trailerLink("https://s3.amazon.com/trailer")
                .description("A test movie")
                .sceneImages(Collections.emptyList())
                .build();

        image = Image.builder()
                .id(1L)
                .generatedName("generated_image_123")
                .fileName("test.jpg")
                .extension("jpg")
                .size(1024L)
                .link("https://amazon.s3.com/images/test.jpg")
                .build();

        sceneImage = SceneImage.builder()
                .id(1L)
                .movie(movie)
                .image(image)
                .build();
        sceneImageResponseDto = new SceneImageResponseDto(
                1L,
                1L,
                new ImageResponseDto(1L, "122-original.jpg", "original.jpg", "jpg", 1024L, "https://s3.amazon.com/images/test.jpg")
        );

        multipartFile = mock(MultipartFile.class);
    }

    @Test
    @DisplayName("Create scene images from valid movie and multipart files")
    void create_shouldSaveSceneImagesWhenValidFilesProvided() {
        List<MultipartFile> files = List.of(multipartFile);
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(imageService.uploadAndBuildOnly("test", multipartFile)).thenReturn(image);
        when(sceneImageRepository.save(any(SceneImage.class))).thenReturn(sceneImage);

        List<SceneImage> result = sceneImageService.create(movie, files);

        assertEquals(1, result.size());
        assertEquals(sceneImage, result.getFirst());
        verify(multipartFile, times(1)).getOriginalFilename();
        verify(imageService, times(1)).uploadAndBuildOnly("test", multipartFile);
        verify(sceneImageRepository, times(1)).save(any(SceneImage.class));
        verify(sceneImageMapper, never()).toDto(any(SceneImage.class));
    }

    @Test
    @DisplayName("Return empty list when multipart files are null")
    void create_shouldReturnEmptyListWhenFilesAreNull() {
        List<SceneImage> result = sceneImageService.create(movie, null);

        assertTrue(result.isEmpty());
        verifyNoInteractions(imageService, sceneImageRepository, sceneImageMapper, multipartFile);
    }

    @Test
    @DisplayName("Return empty list when multipart files are empty")
    void create_shouldReturnEmptyListWhenFilesAreEmpty() {
        List<SceneImage> result = sceneImageService.create(movie, Collections.emptyList());

        assertTrue(result.isEmpty());
        verifyNoInteractions(imageService, sceneImageRepository, sceneImageMapper, multipartFile);
    }

    @Test
    @DisplayName("Delete scene image when it exists")
    void delete_shouldDeleteSceneImageWhenExists() {
        when(sceneImageRepository.existsById(1L)).thenReturn(true);

        sceneImageService.delete(1L);

        verify(sceneImageRepository, times(1)).existsById(1L);
        verify(sceneImageRepository, times(1)).deleteById(1L);
        verifyNoInteractions(imageService, sceneImageMapper);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when deleting non-existent scene image")
    void delete_shouldThrowNotFoundWhenSceneImageDoesNotExist() {
        when(sceneImageRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> sceneImageService.delete(1L));

        verify(sceneImageRepository, times(1)).existsById(1L);
        verify(sceneImageRepository, never()).deleteById(anyLong());
        verifyNoInteractions(imageService, sceneImageMapper);
    }

    @Test
    @DisplayName("Return true when scene image exists by ID")
    void exists_shouldReturnTrueWhenSceneImageExists() {
        when(sceneImageRepository.existsById(1L)).thenReturn(true);

        boolean result = sceneImageService.exists(1L);

        verify(sceneImageRepository, times(1)).existsById(1L);
        assertTrue(result);
        verifyNoInteractions(imageService, sceneImageMapper);
    }

    @Test
    @DisplayName("Return false when scene image does not exist by ID")
    void exists_shouldReturnFalseWhenSceneImageDoesNotExist() {
        when(sceneImageRepository.existsById(1L)).thenReturn(false);

        boolean result = sceneImageService.exists(1L);

        verify(sceneImageRepository, times(1)).existsById(1L);
        assertFalse(result);
        verifyNoInteractions(imageService, sceneImageMapper);
    }

    @Test
    @DisplayName("Find and return SceneImageResponseDto for existing scene image")
    void find_shouldReturnSceneImageDtoWhenSceneImageExists() {
        when(sceneImageRepository.findById(1L)).thenReturn(Optional.of(sceneImage));
        when(sceneImageMapper.toDto(sceneImage)).thenReturn(sceneImageResponseDto);

        SceneImageResponseDto result = sceneImageService.find(1L);

        verify(sceneImageRepository, times(1)).findById(1L);
        verify(sceneImageMapper, times(1)).toDto(sceneImage);
        assertEquals(sceneImageResponseDto, result);
        verifyNoInteractions(imageService);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when finding non-existent scene image")
    void find_shouldThrowNotFoundWhenSceneImageDoesNotExist() {
        when(sceneImageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sceneImageService.find(1L));

        verify(sceneImageRepository, times(1)).findById(1L);
        verify(sceneImageMapper, never()).toDto(any(SceneImage.class));
        verifyNoInteractions(imageService);
    }

    @Test
    @DisplayName("Find and return SceneImage entity for existing scene image")
    void findEntity_shouldReturnSceneImageWhenExists() {
        when(sceneImageRepository.findById(1L)).thenReturn(Optional.of(sceneImage));

        SceneImage result = sceneImageService.findEntity(1L);

        verify(sceneImageRepository, times(1)).findById(1L);
        assertEquals(sceneImage, result);
        verifyNoInteractions(imageService, sceneImageMapper);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when finding non-existent scene image entity")
    void findEntity_shouldThrowNotFoundWhenSceneImageDoesNotExist() {
        when(sceneImageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sceneImageService.findEntity(1L));

        verify(sceneImageRepository, times(1)).findById(1L);
        verifyNoInteractions(imageService, sceneImageMapper);
    }

    @Test
    @DisplayName("Return list of all SceneImageResponseDto")
    void findAll_shouldReturnListOfSceneImageDtos() {
        List<SceneImage> sceneImages = List.of(sceneImage);
        List<SceneImageResponseDto> dtos = List.of(sceneImageResponseDto);
        when(sceneImageRepository.findAll()).thenReturn(sceneImages);
        when(sceneImageMapper.toDtoList(sceneImages)).thenReturn(dtos);

        List<SceneImageResponseDto> result = sceneImageService.findAll();

        verify(sceneImageRepository, times(1)).findAll();
        verify(sceneImageMapper, times(1)).toDtoList(sceneImages);
        assertEquals(dtos, result);
        verifyNoInteractions(imageService);
    }

    @Test
    @DisplayName("Find scene image links by movie ID")
    void findSceneImagesLink_shouldReturnLinksForMovie() {
        List<String> links = List.of("https://moviego.s3/images/test.jpg");
        when(sceneImageRepository.findSceneImagesLink(1L)).thenReturn(links);

        List<String> result = sceneImageService.findSceneImagesLink(1L);

        verify(sceneImageRepository, times(1)).findSceneImagesLink(1L);
        assertEquals(links, result);
        verifyNoInteractions(imageService, sceneImageMapper);
    }
}
