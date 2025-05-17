package com.abbos.moviego.controller;

import com.abbos.moviego.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final MovieService movieService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("movies", movieService.findAllGroupedByCategory());
        model.addAttribute("movieForBanner", movieService.findLastMovie());
        return "common/home";
    }
}
