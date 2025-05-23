package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.CinemaHallCreateDto;
import com.abbos.moviego.dto.CinemaHallResponseDto;
import com.abbos.moviego.dto.CinemaHallUpdateDto;
import com.abbos.moviego.dto.ImageResponseDto;
import com.abbos.moviego.entity.CinemaHall;
import com.abbos.moviego.entity.Image;
import com.abbos.moviego.enums.CinemaHallStatus;
import com.abbos.moviego.mapper.base.UtilMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
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
@SpringBootTest(classes = { CinemaHallMapperImpl.class, ImageMapperImpl.class, UtilMapperImpl.class })
class CinemaHallMapperTest {

    @Autowired
    private CinemaHallMapper cinemaHallMapper;

    @Test
    void testToDto() {
        CinemaHall entity = CinemaHallBuilder.defaultCinemaHall();

        CinemaHallResponseDto dto = cinemaHallMapper.toDto(entity);

        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getName(), dto.name());
        assertEquals(entity.getCapacity(), dto.capacity());
        assertEquals(entity.getRows(), dto.rows());
        assertEquals(entity.getColumns(), dto.columns());
        assertEquals(entity.getStatus().name(), dto.status());

        Image image = entity.getImage();
        assertEquals(image.getId(), dto.image().id());
        assertEquals(image.getGeneratedName(), dto.image().generatedName());
        assertEquals(image.getFileName(), dto.image().fileName());
        assertEquals(image.getExtension(), dto.image().extension());
        assertEquals(image.getSize(), dto.image().size());
        assertEquals(image.getLink(), dto.image().link());
        assertNull(cinemaHallMapper.toDto(null));
    }

    @Test
    void testFromCreate() {
        CinemaHallCreateDto dto = DtoBuilder.defaultCreateDto();

        CinemaHall entity = cinemaHallMapper.fromCreate(dto);

        assertNull(entity.getId());
        assertEquals(dto.name(), entity.getName());
        assertEquals(dto.rows(), entity.getRows());
        assertEquals(dto.columns(), entity.getColumns());
        assertEquals(dto.status(), entity.getStatus());
        assertNull(entity.getImage());
        assertNull(cinemaHallMapper.fromCreate(null));
    }

    @Test
    void testFromUpdate() {
        CinemaHallUpdateDto dto = DtoBuilder.defaultUpdateDto();

        CinemaHall entity = cinemaHallMapper.fromUpdate(dto);

        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.status(), entity.getStatus());
        assertNull(cinemaHallMapper.fromUpdate(null));
    }

    @Test
    void testMergeUpdate() {
        CinemaHallUpdateDto dto = DtoBuilder.defaultUpdateDto();
        CinemaHall target = CinemaHallBuilder.defaultCinemaHall();

        CinemaHall merged = cinemaHallMapper.mergeUpdate(target, dto);

        assertEquals(target.getId(), merged.getId());
        assertEquals(dto.status(), merged.getStatus());
        assertEquals(target, cinemaHallMapper.mergeUpdate(target, null));
    }

    @Test
    void testToDtoList() {
        List<CinemaHall> list = List.of(CinemaHallBuilder.defaultCinemaHall());

        List<CinemaHallResponseDto> dtos = cinemaHallMapper.toDtoList(list);

        assertEquals(1, dtos.size());
        assertEquals(list.getFirst().getId(), dtos.getFirst().id());
        assertNull(cinemaHallMapper.toDtoList(null));
    }

    @Test
    void testToEntityList() {
        List<CinemaHallResponseDto> dtos = List.of(DtoBuilder.defaultResponseDto());

        List<CinemaHall> entities = cinemaHallMapper.toEntityList(dtos);

        assertEquals(1, entities.size());
        assertEquals(dtos.getFirst().id(), entities.getFirst().getId());
        assertNull(cinemaHallMapper.toEntityList(null));
    }

    static class CinemaHallBuilder {
        static CinemaHall defaultCinemaHall() {
            Image image = Image.builder()
                    .id(100L)
                    .generatedName("img_001")
                    .fileName("hall.jpg")
                    .extension("jpg")
                    .size(204800L)
                    .link("https://youtube/...")
                    .build();

            return CinemaHall.builder()
                    .id(1L)
                    .name("Hall A")
                    .capacity(150)
                    .rows(10)
                    .columns(15)
                    .status(CinemaHallStatus.ACTIVE)
                    .image(image)
                    .build();
        }
    }

    static class DtoBuilder {
        static CinemaHallCreateDto defaultCreateDto() {
            return new CinemaHallCreateDto(
                    "Hall B",
                    12,
                    20,
                    CinemaHallStatus.MAINTENANCE,
                    new MockMultipartFile("image", "hall.jpg", "image/jpeg", new byte[]{})
            );
        }

        static CinemaHallUpdateDto defaultUpdateDto() {
            return new CinemaHallUpdateDto(1L, CinemaHallStatus.INACTIVE);
        }

        static CinemaHallResponseDto defaultResponseDto() {
            return new CinemaHallResponseDto(
                    1L,
                    "Hall A",
                    150,
                    10,
                    15,
                    "ACTIVE",
                    new ImageResponseDto(
                            100L,
                            "img_001",
                            "hall.jpg",
                            "jpg",
                            204800L,
                            "https://cdn.example.com/hall.jpg"
                    )
            );
        }
    }
}
