package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.CinemaHallCreateDto;
import com.abbos.moviego.dto.CinemaHallResponseDto;
import com.abbos.moviego.dto.CinemaHallUpdateDto;
import com.abbos.moviego.entity.CinemaHall;
import com.abbos.moviego.entity.Image;
import com.abbos.moviego.enums.CinemaHallStatus;
import com.abbos.moviego.exception.ResourceNotFoundException;
import com.abbos.moviego.mapper.CinemaHallMapper;
import com.abbos.moviego.repository.CinemaHallRepository;
import com.abbos.moviego.service.CinemaHallService;
import com.abbos.moviego.service.ImageService;
import com.abbos.moviego.service.base.AbstractService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.abbos.moviego.util.CacheKeys.CinemaHallKeys.CINEMA_HALL;
import static com.abbos.moviego.util.CacheKeys.CinemaHallKeys.CINEMA_HALLS;
import static com.abbos.moviego.util.CacheKeys.FIND_ALL;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-07
 */
@Service
public class CinemaHallServiceImpl extends AbstractService<CinemaHallRepository, CinemaHallMapper> implements CinemaHallService {

    private final ImageService imageService;
    private final CinemaHallService self;

    public CinemaHallServiceImpl(CinemaHallRepository repository,
                                 CinemaHallMapper cinemaHallMapper,
                                 ImageService imageService,
                                 @Lazy CinemaHallService cinemaHallService) {
        super(repository, cinemaHallMapper);
        this.imageService = imageService;
        this.self = cinemaHallService;
    }

    @CacheEvict(value = CINEMA_HALLS, key = FIND_ALL)
    @Transactional
    @Override
    public void create(CinemaHallCreateDto dto) {
        CinemaHall cinemaHall = mapper.fromCreate(dto);

        Image image = imageService.create(cinemaHall.getName(), dto.image());

        cinemaHall.setImage(image);
        cinemaHall.setCapacity(dto.rows() * dto.columns());

        repository.save(cinemaHall);
    }

    @Caching(evict = {
            @CacheEvict(value = CINEMA_HALL, key = "#dto.id()"),
            @CacheEvict(value = CINEMA_HALLS, key = FIND_ALL)
    })
    @Override
    public void update(CinemaHallUpdateDto dto) {
        CinemaHall cinemaHall = self.findEntity(dto.id());
        cinemaHall.setStatus(dto.status());
        repository.save(cinemaHall);
    }

    @Caching(evict = {
            @CacheEvict(value = CINEMA_HALL, key = "#id"),
            @CacheEvict(value = CINEMA_HALLS, key = FIND_ALL)
    })
    @Override
    public void delete(Long id) {
        if (!exists(id)) {
            throwNotFound(id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CinemaHallResponseDto> findAllByStatusIs(CinemaHallStatus status) {
        return mapper.toDtoList(
                repository.findAllByStatusIs(status)
        );
    }

    @Override
    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    @Override
    public CinemaHallResponseDto find(Long id) {
        return mapper.toDto(self.findEntity(id));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CINEMA_HALL, key = "#id")
    @Override
    public CinemaHall findEntity(Long id) {
        return repository.findById(id).orElseThrow(
                () -> returnNotFound(id)
        );
    }

    @Cacheable(value = CINEMA_HALLS, key = FIND_ALL)
    @Override
    public List<CinemaHallResponseDto> findAll() {
        return mapper.toDtoList(
                repository.findAll()
        );
    }

    private void throwNotFound(Long id) {
        throw returnNotFound(id);
    }

    private ResourceNotFoundException returnNotFound(Long id) {
        return new ResourceNotFoundException("Cinema Hall not found with id: " + id);
    }

}
