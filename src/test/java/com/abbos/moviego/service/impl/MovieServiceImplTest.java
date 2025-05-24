package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.*;
import com.abbos.moviego.entity.Category;
import com.abbos.moviego.entity.Image;
import com.abbos.moviego.entity.Movie;
import com.abbos.moviego.entity.SceneImage;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.MovieMapper;
import com.abbos.moviego.repository.MovieRepository;
import com.abbos.moviego.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieMapper movieMapper;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ImageService imageService;

    @Mock
    private SceneImageService sceneImageService;

    @Mock
    private EventService eventService;

    @Mock
    private MovieService self;

    @InjectMocks
    private MovieServiceImpl movieService;

    private Movie movie;
    private Category category;
    private Image posterImage;
    private SceneImage sceneImage;
    private MovieCreateDto movieCreateDto;
    private MovieUpdateDto movieUpdateDto;
    private MovieResponseDto movieResponseDto;
    private MovieDetailDto movieDetailDto;
    private MultipartFile posterFile;
    private MultipartFile sceneFile;
    private SimpleEventDto simpleEventDto;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1L)
                .name("Action")
                .description("Action movies")
                .build();

        posterImage = Image.builder()
                .id(1L)
                .generatedName("generated_poster_123")
                .fileName("poster.jpg")
                .extension("jpg")
                .size(1024L)
                .link("https://amazons3.moviego.com/images/poster.jpg")
                .build();

        sceneImage = SceneImage.builder()
                .id(1L)
                .image(posterImage)
                .movie(movie)
                .build();

        movie = Movie.builder()
                .id(1L)
                .title("Test Movie")
                .durationMinutes(120)
                .language("English")
                .rating("PG-13")
                .trailerLink("https://s3.amazon.moviego.com/trailer")
                .description("A test movie")
                .category(category)
                .posterImage(posterImage)
                .sceneImages(List.of(sceneImage))
                .createdAt(LocalDateTime.now())
                .build();

        movieResponseDto = new MovieResponseDto(
                1L,
                "Test Movie",
                120,
                "English",
                "PG-13",
                "https://amazon.s3.movigo.com/trailer",
                "A test movie",
                new CategoryResponseDto(1L, "Action", "Action movies"),
                new ImageResponseDto(1L, "123-poster.jpg", "poster.jpg", "jpg", 1024L, "https://amazon.s3.moviego/images/poster.jpg"),
                List.of(new SceneImageResponseDto(1L, 1L, new ImageResponseDto(1L, "123-poster.jpg", "poster.jpg", "jpg", 1024L, "https://amazon.s3.moviego/images/poster.jpg"))),
                LocalDateTime.now()
        );

        movieDetailDto = new MovieDetailDto(
                1L,
                "Test Movie",
                "https://youtube/abbos",
                100,
                "English",
                "PG-13",
                "https://s3.my.com/trailer",
                "A test movie",
                "Horror",
                List.of(),
                List.of());

        simpleEventDto = new SimpleEventDto(1L, LocalDateTime.now());

        posterFile = mock(MultipartFile.class);
        sceneFile = mock(MultipartFile.class);

        movieCreateDto = new MovieCreateDto(
                "Test Movie",
                120,
                "English",
                "PG-13",
                "https://amazon.s3.movigo/trailer",
                "A test movie",
                1L,
                posterFile,
                List.of(sceneFile)
        );

        movieUpdateDto = new MovieUpdateDto(
                1L,
                "Updated Movie",
                130,
                "Action",
                "English",
                "R",
                "https://amazon.s3.moviego/updated-trailer",
                "Updated description"
        );
    }

    @Test
    @DisplayName("Create movie with valid MovieCreateDto and scene images")
    void create_shouldSaveMovieWithPosterAndSceneImages() {
        when(categoryService.findEntity(1L)).thenReturn(category);
        when(posterFile.getOriginalFilename()).thenReturn("poster.jpg");
        when(imageService.create("poster", posterFile)).thenReturn(posterImage);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        when(sceneImageService.create(movie, List.of(sceneFile))).thenReturn(List.of(sceneImage));

        movieService.create(movieCreateDto);

        verify(categoryService, times(1)).findEntity(1L);
        verify(posterFile, times(1)).getOriginalFilename();
        verify(imageService, times(1)).create("poster", posterFile);
        verify(movieRepository, times(1)).save(any(Movie.class));
        verify(sceneImageService, times(1)).create(movie, List.of(sceneFile));
        verifyNoInteractions(movieMapper, eventService, self);
    }

    @Test
    @DisplayName("Create movie without scene images")
    void create_shouldSaveMovieWithoutSceneImages() {
        MovieCreateDto dtoWithoutSceneImages = new MovieCreateDto(
                "Test Movie",
                120,
                "English",
                "PG-13",
                "https://s3.my.com/trailer",
                "A test movie",
                1L,
                posterFile,
                Collections.emptyList()
        );
        when(categoryService.findEntity(1L)).thenReturn(category);
        when(posterFile.getOriginalFilename()).thenReturn("poster.jpg");
        when(imageService.create("poster", posterFile)).thenReturn(posterImage);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        movieService.create(dtoWithoutSceneImages);

        verify(categoryService, times(1)).findEntity(1L);
        verify(posterFile, times(1)).getOriginalFilename();
        verify(imageService, times(1)).create("poster", posterFile);
        verify(movieRepository, times(1)).save(any(Movie.class));
        verify(sceneImageService, never()).create(any(Movie.class), anyList());
        verifyNoInteractions(movieMapper, eventService, self);
    }

    @Test
    @DisplayName("Update movie with valid MovieUpdateDto")
    void update_shouldUpdateMovieWhenValidDtoProvided() {
        when(movieMapper.fromUpdate(movieUpdateDto)).thenReturn(movie);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        movieService.update(movieUpdateDto);

        verify(movieMapper, times(1)).fromUpdate(movieUpdateDto);
        verify(movieRepository, times(1)).save(movie);
        verifyNoInteractions(categoryService, imageService, sceneImageService, eventService, self);
    }

    @Test
    @DisplayName("Delete movie when it exists")
    void delete_shouldDeleteMovieWhenExists() {
        when(movieRepository.existsById(1L)).thenReturn(true);

        movieService.delete(1L);

        verify(movieRepository, times(1)).existsById(1L);
        verify(movieRepository, times(1)).deleteById(1L);
        verifyNoInteractions(movieMapper, categoryService, imageService, sceneImageService, eventService, self);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when deleting non-existent movie")
    void delete_shouldThrowNotFoundWhenMovieDoesNotExist() {
        when(movieRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> movieService.delete(1L));

        verify(movieRepository, times(1)).existsById(1L);
        verify(movieRepository, never()).deleteById(anyLong());
        verifyNoInteractions(movieMapper, categoryService, imageService, sceneImageService, eventService, self);
    }

    @Test
    @DisplayName("Return true when movie exists by ID")
    void exists_shouldReturnTrueWhenMovieExists() {
        when(movieRepository.existsById(1L)).thenReturn(true);

        boolean result = movieService.exists(1L);

        verify(movieRepository, times(1)).existsById(1L);
        assertTrue(result);
    }

    @Test
    @DisplayName("Return false when movie does not exist by ID")
    void exists_shouldReturnFalseWhenMovieDoesNotExist() {
        when(movieRepository.existsById(1L)).thenReturn(false);

        boolean result = movieService.exists(1L);

        verify(movieRepository, times(1)).existsById(1L);
        assertFalse(result);
    }

    @Test
    @DisplayName("Find and return MovieResponseDto for existing movie")
    void find_shouldReturnMovieDtoWhenMovieExists() {
        when(self.findEntity(1L)).thenReturn(movie);
        when(movieMapper.toDto(movie)).thenReturn(movieResponseDto);

        MovieResponseDto result = movieService.find(1L);

        verify(self, times(1)).findEntity(1L);
        verify(movieMapper, times(1)).toDto(movie);
        assertEquals(movieResponseDto, result);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when finding non-existent movie")
    void find_shouldThrowNotFoundWhenMovieDoesNotExist() {
        when(self.findEntity(1L)).thenThrow(new ResourceNotFoundException("Movie not found with id: 1"));

        assertThrows(ResourceNotFoundException.class, () -> movieService.find(1L));

        verify(self, times(1)).findEntity(1L);
        verify(movieMapper, never()).toDto(any(Movie.class));
    }

    @Test
    @DisplayName("Find and return Movie entity for existing movie")
    void findEntity_shouldReturnMovieWhenExists() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        Movie result = movieService.findEntity(1L);

        verify(movieRepository, times(1)).findById(1L);
        assertEquals(movie, result);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when finding non-existent movie entity")
    void findEntity_shouldThrowNotFoundWhenMovieDoesNotExist() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> movieService.findEntity(1L));

        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Return list of all MovieResponseDto with eager loading")
    void findAllEager_shouldReturnListOfMovieDtos() {
        List<Movie> movies = List.of(movie);
        List<MovieResponseDto> dtos = List.of(movieResponseDto);
        when(movieRepository.findAllEager()).thenReturn(movies);
        when(movieMapper.toDtoList(movies)).thenReturn(dtos);

        List<MovieResponseDto> result = movieService.findAllEager();

        verify(movieRepository, times(1)).findAllEager();
        verify(movieMapper, times(1)).toDtoList(movies);
        assertEquals(dtos, result);
    }

    @Test
    @DisplayName("Return movies grouped by category")
    void findAllGroupedByCategory_shouldReturnGroupedMovies() {
        List<MovieResponseDto> dtos = List.of(movieResponseDto);
        when(self.findAllEager()).thenReturn(dtos);

        Map<String, List<MovieResponseDto>> result = movieService.findAllGroupedByCategory();

        verify(self, times(1)).findAllEager();
        assertEquals(1, result.size());
        assertEquals(dtos, result.get("Action"));
    }

    @Test
    @DisplayName("Find last movie details")
    void findLastMovie_shouldReturnLastMovieDetail() {
        PageRequest pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"));
        when(self.fetchMovieDetail(null, pageable, false)).thenReturn(movieDetailDto);

        MovieDetailDto result = movieService.findLastMovie();

        verify(self, times(1)).fetchMovieDetail(null, pageable, false);
        assertEquals(movieDetailDto, result);
    }

    @Test
    @DisplayName("Find movie details by ID with events and scene images")
    void findMovieDetail_shouldReturnMovieDetailWithEventsAndSceneImages() {
        List<String> sceneImageLinks = List.of("https://amazon.my.com/images/scene.jpg");
        List<SimpleEventDto> events = List.of(simpleEventDto);
        when(self.fetchMovieDetail(1L, Pageable.unpaged(), true)).thenReturn(movieDetailDto);
        when(eventService.findSimpleEventDtoByMovieId(1L)).thenReturn(events);
        when(sceneImageService.findSceneImagesLink(1L)).thenReturn(sceneImageLinks);

        MovieDetailDto result = movieService.findMovieDetail(1L);

        verify(self, times(1)).fetchMovieDetail(1L, Pageable.unpaged(), true);
        verify(eventService, times(1)).findSimpleEventDtoByMovieId(1L);
        verify(sceneImageService, times(1)).findSceneImagesLink(1L);
        assertEquals(movieDetailDto, result);
        assertEquals(events, result.getEvents());
        assertEquals(sceneImageLinks, result.getSceneImageLinks());
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when finding non-existent movie detail")
    void findMovieDetail_shouldThrowNotFoundWhenMovieDoesNotExist() {
        when(self.fetchMovieDetail(1L, Pageable.unpaged(), true)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> movieService.findMovieDetail(1L));

        verify(self, times(1)).fetchMovieDetail(1L, Pageable.unpaged(), true);
    }

    @Test
    @DisplayName("Fetch movie details by ID")
    void fetchMovieDetail_shouldReturnMovieDetailWhenExists() {
        List<MovieDetailDto> results = List.of(movieDetailDto);
        when(movieRepository.findMovieBaseDetails(1L, Pageable.unpaged())).thenReturn(results);

        MovieDetailDto result = movieService.fetchMovieDetail(1L, Pageable.unpaged(), true);

        verify(movieRepository, times(1)).findMovieBaseDetails(1L, Pageable.unpaged());
        assertEquals(movieDetailDto, result);
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when fetching non-existent movie detail with throwEx true")
    void fetchMovieDetail_shouldThrowNotFoundWhenMovieDoesNotExistAndThrowExTrue() {
        when(movieRepository.findMovieBaseDetails(1L, Pageable.unpaged())).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> movieService.fetchMovieDetail(1L, Pageable.unpaged(), true));

        verify(movieRepository, times(1)).findMovieBaseDetails(1L, Pageable.unpaged());
    }

    @Test
    @DisplayName("Return null when fetching non-existent movie detail with throwEx false")
    void fetchMovieDetail_shouldReturnNullWhenMovieDoesNotExistAndThrowExFalse() {
        when(movieRepository.findMovieBaseDetails(1L, Pageable.unpaged())).thenReturn(Collections.emptyList());

        MovieDetailDto result = movieService.fetchMovieDetail(1L, Pageable.unpaged(), false);

        verify(movieRepository, times(1)).findMovieBaseDetails(1L, Pageable.unpaged());
        assertNull(result);
    }

    @Test
    @DisplayName("Return list of all MovieResponseDto")
    void findAll_shouldReturnListOfMovieDtos() {
        List<Movie> movies = List.of(movie);
        List<MovieResponseDto> dtos = List.of(movieResponseDto);
        when(movieRepository.findAll()).thenReturn(movies);
        when(movieMapper.toDtoList(movies)).thenReturn(dtos);

        List<MovieResponseDto> result = movieService.findAll();

        verify(movieRepository, times(1)).findAll();
        verify(movieMapper, times(1)).toDtoList(movies);
        assertEquals(dtos, result);
    }
}
