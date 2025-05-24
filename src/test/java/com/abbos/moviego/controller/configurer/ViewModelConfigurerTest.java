package com.abbos.moviego.controller.configurer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-24
 */
class ViewModelConfigurerTest {

    @Test
    void safeConfigureModel_shouldThrowException_whenModelIsNull() {
        ViewModelConfigurer configurer = model -> { /* no-op */ };

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> configurer.safeConfigureModel(null)
        );

        assertEquals("Model cannot be null", exception.getMessage());
    }

    @Test
    void safeConfigureModel_shouldCallConfigureModel_whenModelIsNotNull() {
        Model model = Mockito.mock(Model.class);

        ViewModelConfigurer configurer = Mockito.spy(ViewModelConfigurer.class);
        Mockito.doNothing().when(configurer).configureModel(model);

        configurer.safeConfigureModel(model);

        Mockito.verify(configurer).configureModel(model);
    }
}
