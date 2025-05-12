package com.abbos.moviego.controller;

import com.abbos.moviego.controller.configurer.ViewModelConfigurer;
import com.abbos.moviego.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.abbos.moviego.util.Constants.DASHBOARD_VIEW;
import static com.abbos.moviego.util.Constants.FRAGMENT_KEY;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-10
 */
@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController implements ViewModelConfigurer {

    private final UserService userService;

    @GetMapping
    public String adminDash(Model model) {
        model.addAttribute("user", userService.getMe());
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @Override
    public void configureModel(Model model) {
        model.addAttribute(FRAGMENT_KEY, "profile.html");
    }
}
