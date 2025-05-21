package com.abbos.moviego.entity;

import com.abbos.moviego.entity.base.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Movie extends Auditable {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    @Column(nullable = false)
    private String title;

    @NotNull(message = "Duration is required")
    @Min(1)
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @NotBlank(message = "Language is required")
    @Column(nullable = false)
    private String language;

    @NotBlank(message = "Rating is required")
    @Column(nullable = false)
    private String rating;

    @NotBlank(message = "Trailer link is required")
    @Column(name = "trailer_link", nullable = false, unique = true)
    private String trailerLink;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToOne
    @JoinColumn(name = "poster_image_id")
    private Image posterImage;

    @OneToMany(
            mappedBy = "movie",
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @Column(nullable = false)
    private List<SceneImage> sceneImages;
}
