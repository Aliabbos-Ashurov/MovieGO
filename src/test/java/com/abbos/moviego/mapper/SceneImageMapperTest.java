package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.ImageResponseDto;
import com.abbos.moviego.dto.SceneImageResponseDto;
import com.abbos.moviego.entity.Image;
import com.abbos.moviego.entity.Movie;
import com.abbos.moviego.entity.SceneImage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest(classes = {
        SceneImageMapperImpl.class, MovieMapperImpl.class,
        ImageMapperImpl.class, CategoryMapperImpl.class })
class SceneImageMapperTest {

    @Autowired
    private SceneImageMapper sceneImageMapper;

    @Test
    void testToDto() {
        SceneImage entity = SceneImageBuilder.defaultSceneImage();

        SceneImageResponseDto dto = sceneImageMapper.toDto(entity);

        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getMovie().getId(), dto.movieId());
        assertEquals(entity.getImage().getId(), dto.image().id());
        assertEquals(entity.getImage().getGeneratedName(), dto.image().generatedName());
        assertEquals(entity.getImage().getFileName(), dto.image().fileName());
        assertEquals(entity.getImage().getExtension(), dto.image().extension());
        assertEquals(entity.getImage().getSize(), dto.image().size());
        assertEquals(entity.getImage().getLink(), dto.image().link());
        assertNull(sceneImageMapper.toDto(null));
    }

    @Test
    void testToEntity() {
        SceneImageResponseDto dto = DtoBuilder.defaultSceneImageResponseDto();

        SceneImage entity = sceneImageMapper.toEntity(dto);

        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.movieId(), entity.getMovie().getId());
        assertEquals(dto.image().id(), entity.getImage().getId());
        assertEquals(dto.image().generatedName(), entity.getImage().getGeneratedName());
        assertEquals(dto.image().fileName(), entity.getImage().getFileName());
        assertEquals(dto.image().extension(), entity.getImage().getExtension());
        assertEquals(dto.image().size(), entity.getImage().getSize());
        assertEquals(dto.image().link(), entity.getImage().getLink());
        assertNull(entity.getCreatedAt(), "createdAt should be null as it is set by BaseEntity's @PrePersist");
        assertNull(sceneImageMapper.toEntity(null));
    }

    @Test
    void testToDtoList() {
        List<SceneImage> entities = List.of(SceneImageBuilder.defaultSceneImage());

        List<SceneImageResponseDto> dtos = sceneImageMapper.toDtoList(entities);

        assertEquals(1, dtos.size());
        SceneImageResponseDto dto = dtos.getFirst();
        SceneImage entity = entities.getFirst();
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getMovie().getId(), dto.movieId());
        assertEquals(entity.getImage().getId(), dto.image().id());
        assertEquals(entity.getImage().getGeneratedName(), dto.image().generatedName());
        assertEquals(entity.getImage().getFileName(), dto.image().fileName());
        assertEquals(entity.getImage().getExtension(), dto.image().extension());
        assertEquals(entity.getImage().getSize(), dto.image().size());
        assertEquals(entity.getImage().getLink(), dto.image().link());
        assertNull(sceneImageMapper.toDtoList(null));
    }

    @Test
    void testToEntityList() {
        List<SceneImageResponseDto> dtos = List.of(DtoBuilder.defaultSceneImageResponseDto());

        List<SceneImage> entities = sceneImageMapper.toEntityList(dtos);

        assertEquals(1, entities.size());
        SceneImage entity = entities.getFirst();
        SceneImageResponseDto dto = dtos.getFirst();
        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.movieId(), entity.getMovie().getId());
        assertEquals(dto.image().id(), entity.getImage().getId());
        assertEquals(dto.image().generatedName(), entity.getImage().getGeneratedName());
        assertEquals(dto.image().fileName(), entity.getImage().getFileName());
        assertEquals(dto.image().extension(), entity.getImage().getExtension());
        assertEquals(dto.image().size(), entity.getImage().getSize());
        assertEquals(dto.image().link(), entity.getImage().getLink());
        assertNull(entity.getCreatedAt(), "createdAt should be null as it is set by BaseEntity's @PrePersist");
        assertNull(sceneImageMapper.toEntityList(null));
    }

    static class SceneImageBuilder {
        static SceneImage defaultSceneImage() {
            Movie movie = Movie.builder()
                    .id(1L)
                    .title("Test Movie")
                    .build();
            movie.setCreatedAt(LocalDateTime.now());

            Image image = Image.builder()
                    .id(1L)
                    .generatedName("scene_001")
                    .fileName("scene.jpg")
                    .extension("jpg")
                    .size(102400L)
                    .link("https://cdn.example.com/scene.jpg")
                    .build();
            image.setCreatedAt(LocalDateTime.now());

            SceneImage sceneImage = SceneImage.builder()
                    .id(1L)
                    .movie(movie)
                    .image(image)
                    .build();
            sceneImage.setCreatedAt(LocalDateTime.now());
            return sceneImage;
        }
    }

    static class DtoBuilder {
        static SceneImageResponseDto defaultSceneImageResponseDto() {
            ImageResponseDto imageDto = new ImageResponseDto(
                    1L,
                    "scene_001",
                    "scene.jpg",
                    "jpg",
                    102400L,
                    "https://cdn.example.com/scene.jpg"
            );
            return new SceneImageResponseDto(1L, 1L, imageDto);
        }
    }
}
