package com.abbos.moviego.controller;

import com.abbos.moviego.config.UserPrincipal;
import com.abbos.moviego.controller.configurer.ViewModelConfigurer;
import com.abbos.moviego.dto.UserAddRoleDto;
import com.abbos.moviego.dto.UserResponseDto;
import com.abbos.moviego.dto.UserUpdateDto;
import com.abbos.moviego.service.RoleService;
import com.abbos.moviego.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;

import static com.abbos.moviego.util.Constants.DASHBOARD_VIEW;
import static com.abbos.moviego.util.Constants.FRAGMENT_KEY;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-10
 */
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements ViewModelConfigurer {

    private static final String PROFILE_FRAGMENT = "profile.html";
    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/me")
    public String getMe(Model model) {
        UserResponseDto dto = userService.getMe();
        model.addAttribute("user", dto);
        model.addAttribute(FRAGMENT_KEY, PROFILE_FRAGMENT);
        return DASHBOARD_VIEW;
    }

    @GetMapping
    public String getAll(Model model) {
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @PutMapping
    public String update(@ModelAttribute UserUpdateDto dto, Model model) {
        UserResponseDto updatedDto = userService.update(dto);
        model.addAttribute("user", updatedDto);
        model.addAttribute(FRAGMENT_KEY, PROFILE_FRAGMENT);
        return DASHBOARD_VIEW;
    }

    @PutMapping("/add-role")
    public String addRole(@ModelAttribute UserAddRoleDto dto, Model model) {
        userService.addRole(dto);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, Model model) {
        userService.delete(id);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @PostMapping("/upload")
    public String uploadPhoto(@RequestParam MultipartFile file, Model model) {
        UserResponseDto dto = userService.uploadPhoto(file);
        model.addAttribute("user", dto);
        model.addAttribute(FRAGMENT_KEY, PROFILE_FRAGMENT);
        return DASHBOARD_VIEW;
    }

    @SneakyThrows
    @DeleteMapping("/delete-me/{id}")
    public String deleteMe(@PathVariable Long id,
                           @AuthenticationPrincipal UserPrincipal userPrincipal,
                           HttpServletRequest request) {
        if (!userPrincipal.getEmail().equals(userService.find(id).email())) {
            throw new AccessDeniedException("You can only delete your own account");
        }
        userService.delete(id);
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
        return "redirect:/auth/login";
    }

    @Override
    public void configureModel(Model model) {
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("users", userService.findAll());
        model.addAttribute(FRAGMENT_KEY, "users.html");
    }
}
