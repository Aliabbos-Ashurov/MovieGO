package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.ImageResponseDto;
import com.abbos.moviego.entity.Image;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { ImageMapperImpl.class })
class ImageMapperTest {

    @Autowired
    private ImageMapper imageMapper;

    @Test
    void testToDto() {
        Image entity = ImageBuilder.defaultImage();

        ImageResponseDto dto = imageMapper.toDto(entity);

        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getGeneratedName(), dto.generatedName());
        assertEquals(entity.getFileName(), dto.fileName());
        assertEquals(entity.getExtension(), dto.extension());
        assertEquals(entity.getSize(), dto.size());
        assertEquals(entity.getLink(), dto.link());
        assertNull(imageMapper.toDto(null));
    }

    @Test
    void testToEntity() {
        ImageResponseDto dto = DtoBuilder.defaultImageResponseDto();

        Image entity = imageMapper.toEntity(dto);

        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.generatedName(), entity.getGeneratedName());
        assertEquals(dto.fileName(), entity.getFileName());
        assertEquals(dto.extension(), entity.getExtension());
        assertEquals(dto.size(), entity.getSize());
        assertEquals(dto.link(), entity.getLink());
        assertNull(entity.getCreatedAt(), "createdAt should be set by @PrePersist");
        assertNull(imageMapper.toEntity(null));
    }

    @Test
    void testToDtoList() {
        List<Image> entities = List.of(ImageBuilder.defaultImage());

        List<ImageResponseDto> dtos = imageMapper.toDtoList(entities);

        assertEquals(1, dtos.size());
        ImageResponseDto dto = dtos.getFirst();
        Image entity = entities.getFirst();
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getGeneratedName(), dto.generatedName());
        assertEquals(entity.getFileName(), dto.fileName());
        assertEquals(entity.getExtension(), dto.extension());
        assertEquals(entity.getSize(), dto.size());
        assertEquals(entity.getLink(), dto.link());
        assertNull(imageMapper.toDtoList(null));
    }

    @Test
    void testToEntityList() {
        List<ImageResponseDto> dtos = List.of(DtoBuilder.defaultImageResponseDto());

        List<Image> entities = imageMapper.toEntityList(dtos);

        assertEquals(1, entities.size());
        Image entity = entities.getFirst();
        ImageResponseDto dto = dtos.getFirst();
        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.generatedName(), entity.getGeneratedName());
        assertEquals(dto.fileName(), entity.getFileName());
        assertEquals(dto.extension(), entity.getExtension());
        assertEquals(dto.size(), entity.getSize());
        assertEquals(dto.link(), entity.getLink());
        assertNull(entity.getCreatedAt(), "createdAt should be set by @PrePersist");
        assertNull(imageMapper.toEntityList(null));
    }

    static class ImageBuilder {
        static Image defaultImage() {
            Image image = new Image();
            image.setId(1L);
            image.setGeneratedName("img_001");
            image.setFileName("test_image.jpg");
            image.setExtension("jpg");
            image.setSize(204800L);
            image.setLink("https://cdn.example.com/test_image.jpg");
            image.prePersist(); // Simulate @PrePersist to set createdAt
            return image;
        }
    }

    static class DtoBuilder {
        static ImageResponseDto defaultImageResponseDto() {
            return new ImageResponseDto(
                    1L,
                    "img_001",
                    "test_image.jpg",
                    "jpg",
                    204800L,
                    "https://cdn.example.com/test_image.jpg"
            );
        }
    }
}
