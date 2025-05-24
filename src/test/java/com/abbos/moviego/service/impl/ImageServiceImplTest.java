package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.ImageResponseDto;
import com.abbos.moviego.entity.Image;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.ImageMapper;
import com.abbos.moviego.repository.ImageRepository;
import com.abbos.moviego.service.aws.S3StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageMapper imageMapper;

    @Mock
    private S3StorageService s3StorageService;

    @InjectMocks
    private ImageServiceImpl imageService;

    private Image image;
    private ImageResponseDto imageResponseDto;
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        image = Image.builder()
                .id(1L)
                .generatedName("generated_image_123")
                .fileName("test.jpg")
                .extension("jpg")
                .size(1024L)
                .link("http://example.com/images/test.jpg")
                .build();

        imageResponseDto = new ImageResponseDto(
                1L,
                "generated_image_123",
                "test.jpg",
                "jpg",
                1024L,
                "http://example.com/images/test.jpg"
        );

        file = mock(MultipartFile.class);
    }

    @Test
    @DisplayName("Create image with valid key and file")
    void create_shouldSaveImageWhenValidKeyAndFileProvided() {
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(s3StorageService.uploadObject("key", file)).thenReturn("generated_image_123");
        when(file.getSize()).thenReturn(1024L);
        when(imageRepository.save(any(Image.class))).thenReturn(image);

        Image result = imageService.create("key", file);

        assertEquals(image, result);
        verify(file, times(2)).getOriginalFilename();
        verify(s3StorageService, times(1)).uploadObject("key", file);
        verify(file, times(1)).getSize();
        verify(imageRepository, times(1)).save(any(Image.class));
        verifyNoInteractions(imageMapper);
    }

    @Test
    @DisplayName("Throw RuntimeException when create fails due to S3 upload error")
    void create_shouldThrowRuntimeExceptionWhenUploadFails() {
        when(s3StorageService.uploadObject("key", file)).thenThrow(new RuntimeException("S3 upload failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> imageService.create("key", file));

        assertEquals("Failed to create image", exception.getMessage());
        verifyNoMoreInteractions(s3StorageService);
        verifyNoInteractions(imageRepository, imageMapper);
    }

    @Test
    @DisplayName("Upload and build image without saving")
    void uploadAndBuildOnly_shouldBuildImageWhenValidKeyAndFileProvided() {
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(s3StorageService.uploadObject("key", file)).thenReturn("generated_image_123");
        when(file.getSize()).thenReturn(1024L);

        Image result = imageService.uploadAndBuildOnly("key", file);

        assertEquals("generated_image_123", result.getGeneratedName());
        assertEquals("test.jpg", result.getFileName());
        assertEquals("jpg", result.getExtension());
        assertEquals(1024L, result.getSize());
        verify(file, times(2)).getOriginalFilename();
        verify(s3StorageService, times(1)).uploadObject("key", file);
        verify(file, times(1)).getSize();
        verifyNoInteractions(imageRepository, imageMapper);
    }

    @Test
    @DisplayName("Throw RuntimeException when uploadAndBuildOnly fails due to S3 upload error")
    void uploadAndBuildOnly_shouldThrowRuntimeExceptionWhenUploadFails() {
        when(s3StorageService.uploadObject("key", file)).thenThrow(new RuntimeException("S3 upload failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> imageService.uploadAndBuildOnly("key", file));

        assertEquals("Failed to create image", exception.getMessage());
        verify(s3StorageService, times(1)).uploadObject("key", file);
        verifyNoMoreInteractions(s3StorageService);
        verifyNoInteractions(imageRepository, imageMapper);
    }

    @Test
    @DisplayName("Download image by key")
    void download_shouldReturnByteArrayWhenKeyIsValid() {
        byte[] imageBytes = new byte[]{ 1, 2, 3 };
        when(s3StorageService.downloadObject("key")).thenReturn(imageBytes);

        byte[] result = imageService.download("key");

        assertArrayEquals(imageBytes, result);
        verify(s3StorageService, times(1)).downloadObject("key");
        verifyNoInteractions(imageRepository, imageMapper);
    }

    @Test
    @DisplayName("Delete image when it exists")
    void delete_shouldDeleteImageWhenExists() {
        when(imageRepository.existsById(1L)).thenReturn(true);

        imageService.delete(1L, "key");

        verify(imageRepository, times(1)).existsById(1L);
        verify(s3StorageService, times(1)).deleteObject("key");
        verify(imageRepository, times(1)).deleteById(1L);
        verifyNoInteractions(imageMapper);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when deleting non-existent image")
    void delete_shouldThrowNotFoundWhenImageDoesNotExist() {
        when(imageRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> imageService.delete(1L, "key"));

        verify(imageRepository, times(1)).existsById(1L);
        verify(s3StorageService, never()).deleteObject(anyString());
        verify(imageRepository, never()).deleteById(anyLong());
        verifyNoInteractions(imageMapper);
    }

    @Test
    @DisplayName("Return true when image exists by ID")
    void exists_shouldReturnTrueWhenImageExists() {
        when(imageRepository.existsById(1L)).thenReturn(true);

        boolean result = imageService.exists(1L);

        verify(imageRepository, times(1)).existsById(1L);
        assertTrue(result);
        verifyNoInteractions(s3StorageService, imageMapper);
    }

    @Test
    @DisplayName("Return false when image does not exist by ID")
    void exists_shouldReturnFalseWhenImageDoesNotExist() {
        when(imageRepository.existsById(1L)).thenReturn(false);

        boolean result = imageService.exists(1L);

        verify(imageRepository, times(1)).existsById(1L);
        assertFalse(result);
        verifyNoInteractions(s3StorageService, imageMapper);
    }

    @Test
    @DisplayName("Find and return ImageResponseDto for existing image")
    void find_shouldReturnImageDtoWhenImageExists() {
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
        when(imageMapper.toDto(image)).thenReturn(imageResponseDto);

        ImageResponseDto result = imageService.find(1L);

        verify(imageRepository, times(1)).findById(1L);
        verify(imageMapper, times(1)).toDto(image);
        assertEquals(imageResponseDto, result);
        verifyNoInteractions(s3StorageService);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when finding non-existent image")
    void find_shouldThrowNotFoundWhenImageDoesNotExist() {
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> imageService.find(1L));

        verify(imageRepository, times(1)).findById(1L);
        verify(imageMapper, never()).toDto(any(Image.class));
        verifyNoInteractions(s3StorageService);
    }

    @Test
    @DisplayName("Find and return Image entity for existing image")
    void findEntity_shouldReturnImageWhenExists() {
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

        Image result = imageService.findEntity(1L);

        verify(imageRepository, times(1)).findById(1L);
        assertEquals(image, result);
        verifyNoInteractions(s3StorageService, imageMapper);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when finding non-existent image entity")
    void findEntity_shouldThrowNotFoundWhenImageDoesNotExist() {
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> imageService.findEntity(1L));

        verify(imageRepository, times(1)).findById(1L);
        verifyNoInteractions(s3StorageService, imageMapper);
    }

    @Test
    @DisplayName("Return list of all ImageResponseDto")
    void findAll_shouldReturnListOfImageDtos() {
        List<Image> images = List.of(image);
        List<ImageResponseDto> dtos = List.of(imageResponseDto);
        when(imageRepository.findAll()).thenReturn(images);
        when(imageMapper.toDtoList(images)).thenReturn(dtos);

        List<ImageResponseDto> result = imageService.findAll();

        verify(imageRepository, times(1)).findAll();
        verify(imageMapper, times(1)).toDtoList(images);
        assertEquals(dtos, result);
        verifyNoInteractions(s3StorageService);
    }
}
