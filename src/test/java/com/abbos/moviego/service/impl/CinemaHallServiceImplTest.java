package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.CinemaHallCreateDto;
import com.abbos.moviego.dto.CinemaHallResponseDto;
import com.abbos.moviego.dto.CinemaHallUpdateDto;
import com.abbos.moviego.dto.ImageResponseDto;
import com.abbos.moviego.entity.CinemaHall;
import com.abbos.moviego.entity.Image;
import com.abbos.moviego.enums.CinemaHallStatus;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.CinemaHallMapper;
import com.abbos.moviego.repository.CinemaHallRepository;
import com.abbos.moviego.service.CinemaHallService;
import com.abbos.moviego.service.ImageService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith(MockitoExtension.class)
class CinemaHallServiceImplTest {

    @Mock
    private CinemaHallRepository cinemaHallRepository;

    @Mock
    private CinemaHallMapper cinemaHallMapper;

    @Mock
    private ImageService imageService;

    @Mock
    private CinemaHallService self;

    @InjectMocks
    private CinemaHallServiceImpl cinemaHallService;

    private CinemaHall cinemaHall;
    private Image image;
    private CinemaHallCreateDto cinemaHallCreateDto;
    private CinemaHallUpdateDto cinemaHallUpdateDto;
    private CinemaHallResponseDto cinemaHallResponseDto;
    private MultipartFile imageFile;

    @BeforeEach
    void setUp() {
        image = Image.builder()
                .id(1L)
                .generatedName("generated_image_123")
                .fileName("hall_image.jpg")
                .extension("jpg")
                .size(1024L)
                .link("https://amazons3.com/images/hall_image.jpg")
                .build();

        cinemaHall = CinemaHall.builder()
                .id(1L)
                .name("Hall 1")
                .capacity(50)
                .rows(5)
                .columns(10)
                .status(CinemaHallStatus.ACTIVE)
                .image(image)
                .build();

        cinemaHallCreateDto = new CinemaHallCreateDto(
                "Hall 1",
                5,
                10,
                CinemaHallStatus.ACTIVE,
                null
        );

        cinemaHallUpdateDto = new CinemaHallUpdateDto(
                1L,
                CinemaHallStatus.MAINTENANCE
        );

        cinemaHallResponseDto = new CinemaHallResponseDto(
                1L,
                "Hall 1",
                50,
                5,
                10,
                CinemaHallStatus.ACTIVE.name(),
                new ImageResponseDto(1L, "123-hall_image.jpg", "hall_image.jpg", "jpg", 1024L, "http://example.com/images/hall_image.jpg")
        );

        imageFile = mock(MultipartFile.class);
    }

    @Test
    @DisplayName("Create cinema hall with valid CinemaHallCreateDto")
    void create_shouldSaveCinemaHallWhenValidDtoProvided() {
        when(cinemaHallMapper.fromCreate(cinemaHallCreateDto)).thenReturn(cinemaHall);
        when(imageService.create(eq("Hall 1"), isNull())).thenReturn(image);
        when(cinemaHallRepository.save(any(CinemaHall.class))).thenReturn(cinemaHall);

        cinemaHallService.create(cinemaHallCreateDto);

        verify(cinemaHallMapper).fromCreate(cinemaHallCreateDto);
        verify(imageService).create(eq("Hall 1"), isNull());
        verify(cinemaHallRepository).save(cinemaHall);

        assertEquals(image, cinemaHall.getImage());
        assertEquals(50, cinemaHall.getCapacity());
        verifyNoInteractions(self);
    }


    @Test
    @DisplayName("Update cinema hall status with valid CinemaHallUpdateDto")
    void update_shouldUpdateCinemaHallStatusWhenValidDtoProvided() {
        when(self.findEntity(1L)).thenReturn(cinemaHall);
        when(cinemaHallRepository.save(any(CinemaHall.class))).thenReturn(cinemaHall);

        cinemaHallService.update(cinemaHallUpdateDto);

        verify(self, times(1)).findEntity(1L);
        verify(cinemaHallRepository, times(1)).save(cinemaHall);
        assertEquals(CinemaHallStatus.MAINTENANCE, cinemaHall.getStatus());
        verifyNoInteractions(cinemaHallMapper, imageService);
    }

    @Test
    @DisplayName("Delete cinema hall when it exists")
    void delete_shouldDeleteCinemaHallWhenExists() {
        when(cinemaHallRepository.existsById(1L)).thenReturn(true);

        cinemaHallService.delete(1L);

        verify(cinemaHallRepository, times(1)).existsById(1L);
        verify(cinemaHallRepository, times(1)).deleteById(1L);
        verifyNoInteractions(cinemaHallMapper, imageService, self);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when deleting non-existent cinema hall")
    void delete_shouldThrowNotFoundWhenCinemaHallDoesNotExist() {
        when(cinemaHallRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> cinemaHallService.delete(1L));

        verify(cinemaHallRepository, times(1)).existsById(1L);
        verify(cinemaHallRepository, never()).deleteById(anyLong());
        verifyNoInteractions(cinemaHallMapper, imageService, self);
    }

    @Test
    @DisplayName("Find all cinema halls by status")
    void findAllByStatus_shouldReturnCinemaHallDtosByStatus() {
        List<CinemaHall> cinemaHalls = List.of(cinemaHall);
        List<CinemaHallResponseDto> dtos = List.of(cinemaHallResponseDto);
        when(cinemaHallRepository.findAllByStatusIs(CinemaHallStatus.ACTIVE)).thenReturn(cinemaHalls);
        when(cinemaHallMapper.toDtoList(cinemaHalls)).thenReturn(dtos);

        List<CinemaHallResponseDto> result = cinemaHallService.findAllByStatusIs(CinemaHallStatus.ACTIVE);

        verify(cinemaHallRepository, times(1)).findAllByStatusIs(CinemaHallStatus.ACTIVE);
        verify(cinemaHallMapper, times(1)).toDtoList(cinemaHalls);
        assertEquals(dtos, result);
        verifyNoInteractions(imageService, self);
    }

    @Test
    @DisplayName("Return true when cinema hall exists by ID")
    void exists_shouldReturnTrueWhenCinemaHallExists() {
        when(cinemaHallRepository.existsById(1L)).thenReturn(true);

        boolean result = cinemaHallService.exists(1L);

        verify(cinemaHallRepository, times(1)).existsById(1L);
        assertTrue(result);
        verifyNoInteractions(cinemaHallMapper, imageService, self);
    }

    @Test
    @DisplayName("Return false when cinema hall does not exist by ID")
    void exists_shouldReturnFalseWhenCinemaHallDoesNotExist() {
        when(cinemaHallRepository.existsById(1L)).thenReturn(false);

        boolean result = cinemaHallService.exists(1L);

        verify(cinemaHallRepository, times(1)).existsById(1L);
        assertFalse(result);
        verifyNoInteractions(cinemaHallMapper, imageService, self);
    }

    @Test
    @DisplayName("Find and return CinemaHallResponseDto for existing cinema hall")
    void find_shouldReturnCinemaHallDtoWhenCinemaHallExists() {
        when(self.findEntity(1L)).thenReturn(cinemaHall);
        when(cinemaHallMapper.toDto(cinemaHall)).thenReturn(cinemaHallResponseDto);

        CinemaHallResponseDto result = cinemaHallService.find(1L);

        verify(self, times(1)).findEntity(1L);
        verify(cinemaHallMapper, times(1)).toDto(cinemaHall);
        assertEquals(cinemaHallResponseDto, result);
        verifyNoInteractions(cinemaHallRepository, imageService);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when finding non-existent cinema hall")
    void find_shouldThrowNotFoundWhenCinemaHallDoesNotExist() {
        when(self.findEntity(1L)).thenThrow(new ResourceNotFoundException("Cinema Hall not found with id: 1"));

        assertThrows(ResourceNotFoundException.class, () -> cinemaHallService.find(1L));

        verify(self, times(1)).findEntity(1L);
        verify(cinemaHallMapper, never()).toDto(any(CinemaHall.class));
        verifyNoInteractions(cinemaHallRepository, imageService);
    }

    @Test
    @DisplayName("Find and return CinemaHall entity for existing cinema hall")
    void findEntity_shouldReturnCinemaHallWhenExists() {
        when(cinemaHallRepository.findById(1L)).thenReturn(Optional.of(cinemaHall));

        CinemaHall result = cinemaHallService.findEntity(1L);

        verify(cinemaHallRepository, times(1)).findById(1L);
        assertEquals(cinemaHall, result);
        verifyNoInteractions(cinemaHallMapper, imageService, self);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when finding non-existent cinema hall entity")
    void findEntity_shouldThrowNotFoundWhenCinemaHallDoesNotExist() {
        when(cinemaHallRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cinemaHallService.findEntity(1L));

        verify(cinemaHallRepository, times(1)).findById(1L);
        verifyNoInteractions(cinemaHallMapper, imageService, self);
    }

    @Test
    @DisplayName("Return list of all CinemaHallResponseDto")
    void findAll_shouldReturnListOfCinemaHallDtos() {
        List<CinemaHall> cinemaHalls = List.of(cinemaHall);
        List<CinemaHallResponseDto> dtos = List.of(cinemaHallResponseDto);
        when(cinemaHallRepository.findAll()).thenReturn(cinemaHalls);
        when(cinemaHallMapper.toDtoList(cinemaHalls)).thenReturn(dtos);

        List<CinemaHallResponseDto> result = cinemaHallService.findAll();

        verify(cinemaHallRepository, times(1)).findAll();
        verify(cinemaHallMapper, times(1)).toDtoList(cinemaHalls);
        assertEquals(dtos, result);
        verifyNoInteractions(imageService, self);
    }
}
