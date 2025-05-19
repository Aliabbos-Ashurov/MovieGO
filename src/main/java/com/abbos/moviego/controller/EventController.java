package com.abbos.moviego.controller;

import com.abbos.moviego.controller.configurer.ViewModelConfigurer;
import com.abbos.moviego.dto.EventCreateDto;
import com.abbos.moviego.dto.EventUpdateDto;
import com.abbos.moviego.enums.CinemaHallStatus;
import com.abbos.moviego.enums.EventStatus;
import com.abbos.moviego.service.CinemaHallService;
import com.abbos.moviego.service.EventService;
import com.abbos.moviego.service.MovieService;
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
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController implements ViewModelConfigurer {

    private final EventService eventService;
    private final CinemaHallService cinemaHallService;
    private final MovieService movieService;

    @GetMapping("/active")
    public String getActiveEvents(Model model) {
        model.addAttribute("events", eventService.findAllByStatus(EventStatus.SCHEDULED));
        return "active-events";
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_EVENT')")
    public String getAll(Model model) {
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_EVENT')")
    public String create(@Valid @ModelAttribute EventCreateDto dto, Model model) {
        eventService.create(dto);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @PutMapping
    @PreAuthorize("hasAuthority('UPDATE_EVENT')")
    public String updateStatus(@Valid @ModelAttribute EventUpdateDto dto, Model model) {
        eventService.update(dto);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_EVENT')")
    public String delete(@PathVariable Long id, Model model) {
        eventService.delete(id);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @Override
    public void configureModel(Model model) {
        model.addAttribute("events", eventService.findAllEager());
        model.addAttribute("cinemaHalls", cinemaHallService.findAllByStatusIs(CinemaHallStatus.ACTIVE));
        model.addAttribute("movies", movieService.findAllEager());
        model.addAttribute("eventStatuses", EventStatus.values());
        model.addAttribute(FRAGMENT_KEY, "events.html");
    }
}
