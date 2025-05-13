package com.abbos.moviego.controller;

import com.abbos.moviego.controller.configurer.ViewModelConfigurer;
import com.abbos.moviego.dto.CinemaHallCreateDto;
import com.abbos.moviego.dto.CinemaHallUpdateDto;
import com.abbos.moviego.enums.CinemaHallStatus;
import com.abbos.moviego.service.CinemaHallService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.abbos.moviego.util.Constants.DASHBOARD_VIEW;
import static com.abbos.moviego.util.Constants.FRAGMENT_KEY;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-13
 */
@Controller
@RequestMapping("/cinema-halls")
@RequiredArgsConstructor
public class CinemaHallController implements ViewModelConfigurer {

    private final CinemaHallService cinemaHallService;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_CINEMA_HALL')")
    public String getAll(Model model) {
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_CINEMA_HALL')")
    public String create(@Valid @ModelAttribute CinemaHallCreateDto dto, Model model) {
        cinemaHallService.create(dto);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @PutMapping
    @PreAuthorize("hasAuthority('UPDATE_CINEMA_HALL')")
    public String update(@Valid @ModelAttribute CinemaHallUpdateDto dto, Model model) {
        cinemaHallService.update(dto);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_CINEMA_HALL')")
    public String delete(@PathVariable Long id, Model model) {
        cinemaHallService.delete(id);
        configureModel(model);
        return DASHBOARD_VIEW;
    }


    @Override
    public void configureModel(Model model) {
        model.addAttribute("cinemaHalls", cinemaHallService.findAll());
        model.addAttribute("cinemaHallStatuses", CinemaHallStatus.values());
        model.addAttribute(FRAGMENT_KEY, "cinema-halls.html");
    }
}
