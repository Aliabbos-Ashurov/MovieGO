package com.abbos.moviego.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * We considered to using class not record because of
 * will set (posterImageLink, sceneImageLinks, events) at runtime, we need setter process
 *
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-17
 */
@Getter
@AllArgsConstructor
public class MovieDetailDto implements Serializable {
    private Long id;
    private String title;
    private String trailerLink;
    private Integer durationMinutes;
    private String language;
    private String rating;
    private String description;
    private String categoryName;
    private String posterImageLink;

    @Setter
    private List<String> sceneImageLinks;

    @Setter
    private List<SimpleEventDto> events;
}
