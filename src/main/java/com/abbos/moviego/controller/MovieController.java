package com.abbos.moviego.controller;

import com.abbos.moviego.controller.configurer.ViewModelConfigurer;
import com.abbos.moviego.dto.MovieCreateDto;
import com.abbos.moviego.dto.MovieUpdateDto;
import com.abbos.moviego.service.CategoryService;
import com.abbos.moviego.service.MovieService;
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
 * @since 2025-05-09
 */
@Controller
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController implements ViewModelConfigurer {

    private final MovieService movieService;
    private final CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_MOVIE')")
    public String getAll(Model model) {
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable Long id, Model model) {
        model.addAttribute("movie", movieService.findMovieDetail(id));
        return "movie";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_MOVIE')")
    public String create(@ModelAttribute MovieCreateDto dto, Model model) {
        movieService.create(dto);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @PutMapping
    @PreAuthorize("hasAuthority('UPDATE_MOVIE')")
    public String updateMovie(@ModelAttribute MovieUpdateDto dto, Model model) {
        movieService.update(dto);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_MOVIE')")
    public String deleteMovie(@PathVariable Long id, Model model) {
        movieService.delete(id);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @Override
    public void configureModel(Model model) {
        model.addAttribute("movies", movieService.findAllEager());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute(FRAGMENT_KEY, "movies.html");
    }
}
