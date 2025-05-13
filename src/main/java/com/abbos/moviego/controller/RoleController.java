package com.abbos.moviego.controller;

import com.abbos.moviego.controller.configurer.ViewModelConfigurer;
import com.abbos.moviego.dto.RoleCreateDto;
import com.abbos.moviego.dto.RoleUpdateDto;
import com.abbos.moviego.service.PermissionService;
import com.abbos.moviego.service.RoleService;
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
 * @since 2025-05-11
 */
@Controller
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController implements ViewModelConfigurer {

    private final RoleService roleService;
    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ROLE')")
    public String getAll(Model model) {
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    public String create(@ModelAttribute RoleCreateDto dto, Model model) {
        roleService.create(dto);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @PutMapping
    @PreAuthorize("hasAuthority('UPDATE_ROLE')")
    public String update(@ModelAttribute RoleUpdateDto dto, Model model) {
        roleService.update(dto);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    public String delete(@PathVariable Long id, Model model) {
        roleService.delete(id);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @Override
    public void configureModel(Model model) {
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("permissions", permissionService.findAll());
        model.addAttribute(FRAGMENT_KEY, "roles.html");
    }
}

