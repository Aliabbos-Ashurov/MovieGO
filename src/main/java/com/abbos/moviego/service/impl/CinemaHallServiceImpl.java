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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-07
 */
@Service
public class CinemaHallServiceImpl extends AbstractService<CinemaHallRepository, CinemaHallMapper> implements CinemaHallService {

    private final ImageService imageService;

    public CinemaHallServiceImpl(CinemaHallRepository repository,
                                 CinemaHallMapper cinemaHallMapper,
                                 ImageService imageService) {
        super(repository, cinemaHallMapper);
        this.imageService = imageService;
    }

    @Transactional
    @Override
    public void create(CinemaHallCreateDto dto) {
        CinemaHall cinemaHall = mapper.fromCreate(dto);

        Image image = imageService.create(cinemaHall.getName(), dto.image());

        cinemaHall.setImage(image);
        cinemaHall.setCapacity(dto.rows() * dto.columns());

        repository.save(cinemaHall);
    }

    @Override
    public void update(CinemaHallUpdateDto dto) {
        CinemaHall cinemaHall = findEntity(dto.id());
        cinemaHall.setStatus(dto.status());
        repository.save(cinemaHall);
    }

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
        return mapper.toDto(findEntity(id));
    }

    @Override
    public CinemaHall findEntity(Long id) {
        return repository.findById(id).orElseThrow(
                () -> returnNotFound(id)
        );
    }

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
