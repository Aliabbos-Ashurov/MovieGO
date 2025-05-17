package com.abbos.moviego.controller;

import com.abbos.moviego.controller.configurer.ViewModelConfigurer;
import com.abbos.moviego.dto.UserAddRoleDto;
import com.abbos.moviego.dto.UserUpdateDto;
import com.abbos.moviego.service.RoleService;
import com.abbos.moviego.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

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
        model.addAttribute("user", userService.getMe());
        model.addAttribute(FRAGMENT_KEY, PROFILE_FRAGMENT);
        return DASHBOARD_VIEW;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_USER')")
    public String getAll(Model model) {
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @PutMapping
    @PreAuthorize("hasAuthority('EDIT_PROFILE')")
    public String updatePassword(@ModelAttribute UserUpdateDto dto,
                                 HttpServletRequest httpServletRequest,
                                 HttpServletResponse httpServletResponse) {
        userService.update(dto);
        logout(httpServletRequest, httpServletResponse);
        return "common/auth";
    }

    @PutMapping("/add-role")
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    public String addRole(@ModelAttribute UserAddRoleDto dto, Model model) {
        userService.addRole(dto);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public String delete(@PathVariable Long id, Model model) {
        userService.delete(id);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('EDIT_PROFILE')")
    public String uploadPhoto(@RequestParam MultipartFile file, Model model) {
        model.addAttribute("user", userService.uploadPhoto(file));
        model.addAttribute(FRAGMENT_KEY, PROFILE_FRAGMENT);
        return DASHBOARD_VIEW;
    }

    @SneakyThrows
    @DeleteMapping("/delete-me/{id}")
    @PreAuthorize("hasAuthority('EDIT_PROFILE')")
    public String deleteMe(@PathVariable Long id,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        userService.delete(id);
        logout(request, response);
        return "redirect:/auth/login";
    }

    @Override
    public void configureModel(Model model) {
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("users", userService.findAll());
        model.addAttribute(FRAGMENT_KEY, "users.html");
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication)) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
    }
}
