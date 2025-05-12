package com.abbos.moviego.controller;

import com.abbos.moviego.dto.auth.SignUpDto;
import com.abbos.moviego.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-06
 */
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) String error) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", error);
        modelAndView.setViewName("auth");
        return modelAndView;
    }

    @GetMapping("/signup")
    public String signUp() {
        return "auth";
    }

    @PostMapping("/signup")
    public String signUpPost(@Valid @ModelAttribute SignUpDto dto) {
        userService.create(dto);
        return "auth";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }
}
