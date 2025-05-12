package com.abbos.moviego.controller.configurer;

import org.springframework.ui.Model;

/**
 * Functional interface for setting up a {@link Model} with attributes for a Thymeleaf view.
 * Used in controllers to populate model data in a reusable way.
 *
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-12
 */
@FunctionalInterface
public interface ViewModelConfigurer {

    /**
     * Adds attributes to the given {@link Model} for view rendering.
     *
     * @param model the model to configure
     */
    void configureModel(Model model);
}

