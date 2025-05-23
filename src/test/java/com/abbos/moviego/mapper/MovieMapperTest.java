package com.abbos.moviego.mapper;


import com.abbos.moviego.dto.*;
import com.abbos.moviego.entity.Category;
import com.abbos.moviego.entity.Image;
import com.abbos.moviego.entity.Movie;
import com.abbos.moviego.entity.SceneImage;
import com.abbos.moviego.mapper.base.UtilMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { MovieMapperImpl.class, CategoryMapperImpl.class, ImageMapperImpl.class, SceneImageMapperImpl.class, UtilMapperImpl.class })
class MovieMapperTest {

    @Autowired
    private MovieMapper movieMapper;


    @Test
    void testToDto() {
        Movie entity = MovieBuilder.defaultMovie();

        MovieResponseDto dto = movieMapper.toDto(entity);

        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getTitle(), dto.title());
        assertEquals(entity.getDurationMinutes(), dto.durationMinutes());
        assertEquals(entity.getLanguage(), dto.language());
        assertEquals(entity.getRating(), dto.rating());
        assertEquals(entity.getTrailerLink(), dto.trailerLink());
        assertEquals(entity.getDescription(), dto.description());
        assertEquals(entity.getCategory().getId(), dto.category().id());
        assertEquals(entity.getPosterImage().getId(), dto.posterImage().id());
        assertEquals(entity.getSceneImages().size(), dto.sceneImages().size());
        assertEquals(entity.getSceneImages().getFirst().getImage().getId(), dto.sceneImages().getFirst().image().id());
        assertEquals(entity.getCreatedAt(), dto.createdAt());
        assertNull(movieMapper.toDto(null));
    }

    @Test
    void testFromCreate() {
        MovieCreateDto dto = DtoBuilder.defaultCreateDto();

        Movie entity = movieMapper.fromCreate(dto);

        assertNull(entity.getId());
        assertEquals(dto.title(), entity.getTitle());
        assertEquals(dto.durationMinutes(), entity.getDurationMinutes());
        assertEquals(dto.language(), entity.getLanguage());
        assertEquals(dto.rating(), entity.getRating());
        assertEquals(dto.trailerLink(), entity.getTrailerLink());
        assertEquals(dto.description(), entity.getDescription());
        assertNull(entity.getCategory(), "Category should be ignored per mapping");
        assertNull(entity.getPosterImage(), "PosterImage should be ignored per mapping");
        assertNull(entity.getSceneImages(), "SceneImages should be ignored per mapping");
        assertNull(entity.getCreatedAt(), "createdAt should be set by Auditable");
        assertNull(movieMapper.fromCreate(null));
    }

    @Test
    void testFromUpdate() {
        MovieUpdateDto dto = DtoBuilder.defaultUpdateDto();

        Movie entity = movieMapper.fromUpdate(dto);

        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.title(), entity.getTitle());
        assertEquals(dto.durationMinutes(), entity.getDurationMinutes());
        assertEquals(dto.language(), entity.getLanguage());
        assertEquals(dto.rating(), entity.getRating());
        assertEquals(dto.trailerLink(), entity.getTrailerLink());
        assertEquals(dto.description(), entity.getDescription());
        assertNull(entity.getCategory());
        assertNull(entity.getPosterImage());
        assertNull(entity.getSceneImages());
        assertNull(entity.getCreatedAt(), "createdAt should be set by Auditable");
        assertNull(movieMapper.fromUpdate(null));
    }

    @Test
    void testMergeUpdate() {
        MovieUpdateDto dto = DtoBuilder.defaultUpdateDto();
        Movie target = MovieBuilder.defaultMovie();

        Movie merged = movieMapper.mergeUpdate(target, dto);

        assertEquals(target.getId(), merged.getId());
        assertEquals(dto.title(), merged.getTitle());
        assertEquals(dto.durationMinutes(), merged.getDurationMinutes());
        assertEquals(dto.language(), merged.getLanguage());
        assertEquals(dto.rating(), merged.getRating());
        assertEquals(dto.trailerLink(), merged.getTrailerLink());
        assertEquals(dto.description(), merged.getDescription());
        assertEquals(target.getCategory(), merged.getCategory());
        assertEquals(target.getPosterImage(), merged.getPosterImage());
        assertEquals(target.getSceneImages(), merged.getSceneImages());
        assertEquals(target.getCreatedAt(), merged.getCreatedAt());
        assertEquals(target, movieMapper.mergeUpdate(target, null));
    }

    @Test
    void testToDtoList() {
        List<Movie> entities = List.of(MovieBuilder.defaultMovie());

        List<MovieResponseDto> dtos = movieMapper.toDtoList(entities);

        assertEquals(1, dtos.size());
        MovieResponseDto dto = dtos.getFirst();
        Movie entity = entities.getFirst();
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getTitle(), dto.title());
        assertEquals(entity.getDurationMinutes(), dto.durationMinutes());
        assertEquals(entity.getLanguage(), dto.language());
        assertEquals(entity.getRating(), dto.rating());
        assertEquals(entity.getTrailerLink(), dto.trailerLink());
        assertEquals(entity.getDescription(), dto.description());
        assertEquals(entity.getCategory().getId(), dto.category().id());
        assertEquals(entity.getPosterImage().getId(), dto.posterImage().id());
        assertEquals(entity.getSceneImages().size(), dto.sceneImages().size());
        assertEquals(entity.getSceneImages().getFirst().getImage().getId(), dto.sceneImages().getFirst().image().id());
        assertEquals(entity.getCreatedAt(), dto.createdAt());
        assertNull(movieMapper.toDtoList(null));
    }

    @Test
    void testToEntityList() {
        List<MovieResponseDto> dtos = List.of(DtoBuilder.defaultResponseDto());

        List<Movie> entities = movieMapper.toEntityList(dtos);

        assertEquals(1, entities.size());
        Movie entity = entities.getFirst();
        MovieResponseDto dto = dtos.getFirst();
        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.title(), entity.getTitle());
        assertEquals(dto.durationMinutes(), entity.getDurationMinutes());
        assertEquals(dto.language(), entity.getLanguage());
        assertEquals(dto.rating(), entity.getRating());
        assertEquals(dto.trailerLink(), entity.getTrailerLink());
        assertEquals(dto.description(), entity.getDescription());
        assertEquals(dto.category().id(), entity.getCategory().getId());
        assertEquals(dto.posterImage().id(), entity.getPosterImage().getId());
        assertEquals(dto.sceneImages().size(), entity.getSceneImages().size());
        assertEquals(dto.createdAt(), entity.getCreatedAt());
        assertNull(movieMapper.toEntityList(null));
    }

    static class MovieBuilder {
        static Movie defaultMovie() {
            Category category = Category.builder()
                    .id(1L)
                    .name("Action")
                    .build();

            Image image = Image.builder()
                    .id(1L)
                    .generatedName("image_001")
                    .fileName("image.jpg")
                    .extension("jpg")
                    .size(204800L)
                    .link("https://cdn.example.com/image.jpg")
                    .build();

            SceneImage sceneImage = SceneImage.builder()
                    .id(1L)
                    .image(image)
                    .build();

            Movie movie = Movie.builder()
                    .id(1L)
                    .title("Test Movie")
                    .durationMinutes(120)
                    .language("English")
                    .rating("PG-13")
                    .trailerLink("https://youtube.com/trailer")
                    .description("A thrilling action movie")
                    .category(category)
                    .posterImage(image)
                    .sceneImages(List.of(sceneImage))
                    .build();
            movie.setCreatedAt(LocalDateTime.now());
            return movie;
        }
    }

    static class DtoBuilder {
        static MovieCreateDto defaultCreateDto() {
            return new MovieCreateDto(
                    "Test Movie",
                    120,
                    "English",
                    "PG-13",
                    "https://youtube.com/trailer",
                    "A thrilling action movie",
                    1L,
                    new MockMultipartFile("posterImage", "poster.jpg", "image/jpeg", new byte[]{}),
                    List.of(new MockMultipartFile("sceneImage", "scene.jpg", "image/jpeg", new byte[]{}))
            );
        }

        static MovieUpdateDto defaultUpdateDto() {
            return new MovieUpdateDto(
                    1L,
                    "Updated Movie",
                    130,
                    "Action",
                    "Spanish",
                    "R",
                    "https://youtube.com/updated_trailer",
                    "An updated thrilling movie"
            );
        }

        static MovieResponseDto defaultResponseDto() {
            CategoryResponseDto categoryDto = new CategoryResponseDto(1L, "Action", "Action Description");
            ImageResponseDto imageDto = new ImageResponseDto(1L, "image_001", "image.jpg", "jpg", 204800L, "https://cdn.example.com/image.jpg");
            SceneImageResponseDto sceneImageDto = new SceneImageResponseDto(1L, 1L, null);
            return new MovieResponseDto(
                    1L,
                    "Test Movie",
                    120,
                    "English",
                    "PG-13",
                    "https://youtube.com/trailer",
                    "A thrilling action movie",
                    categoryDto,
                    imageDto,
                    List.of(sceneImageDto),
                    LocalDateTime.now()
            );
        }
    }
}