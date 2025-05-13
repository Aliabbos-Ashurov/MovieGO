package com.abbos.moviego.controller;

import com.abbos.moviego.controller.configurer.ViewModelConfigurer;
import com.abbos.moviego.dto.PermissionCreateDto;
import com.abbos.moviego.dto.PermissionUpdateDto;
import com.abbos.moviego.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.abbos.moviego.util.Constants.DASHBOARD_VIEW;
import static com.abbos.moviego.util.Constants.FRAGMENT_KEY;

/**
 * Controller for managing permissions in the MovieGo application.
 *
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-11
 */
@Controller
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController implements ViewModelConfigurer {

    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_PERMISSION')")
    public String getAll(Model model) {
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_PERMISSION')")
    public String create(@ModelAttribute PermissionCreateDto dto, Model model) {
        permissionService.create(dto);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @PutMapping
    @PreAuthorize("hasAuthority('UPDATE_PERMISSION')")
    public String update(@ModelAttribute PermissionUpdateDto dto, Model model) {
        permissionService.update(dto);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_PERMISSION')")
    public String update(@PathVariable Long id, Model model) {
        permissionService.delete(id);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @Override
    public void configureModel(Model model) {
        model.addAttribute("permissions", permissionService.findAll());
        model.addAttribute(FRAGMENT_KEY, "permissions.html");
    }
}
