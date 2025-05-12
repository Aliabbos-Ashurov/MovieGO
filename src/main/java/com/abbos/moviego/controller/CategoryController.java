package com.abbos.moviego.controller;

import com.abbos.moviego.controller.configurer.ViewModelConfigurer;
import com.abbos.moviego.dto.CategoryCreateDto;
import com.abbos.moviego.dto.CategoryUpdateDto;
import com.abbos.moviego.service.CategoryService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController implements ViewModelConfigurer {

    private final CategoryService categoryService;

    @GetMapping
    public String getAll(Model model) {
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @PostMapping
    public String create(@ModelAttribute CategoryCreateDto dto, Model model) {
        categoryService.create(dto);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @PutMapping
    public String update(@ModelAttribute CategoryUpdateDto dto, Model model) {
        categoryService.update(dto);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, Model model) {
        categoryService.delete(id);
        configureModel(model);
        return DASHBOARD_VIEW;
    }

    @Override
    public void configureModel(Model model) {
        model.addAttribute("category", categoryService.findAll());
        model.addAttribute(FRAGMENT_KEY, "category.html");
    }
}
