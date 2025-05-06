package com.abbos.moviego.entity;

import com.abbos.moviego.enums.CinemaHallStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
@Entity
@Table(name = "cinema_hall")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CinemaHall extends Auditable {

    @NotBlank(message = "Cinema Hall name is required")
    @Size(max = 50, message = "Name must not exceed {max} characters")
    @Column(nullable = false)
    private String name;

    @Positive
    @Column(nullable = false)
    private Integer capacity;

    @Positive
    @Column(nullable = false)
    private Integer rows;

    @Positive
    @Column(nullable = false)
    private Integer columns;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CinemaHallStatus status;

    @OneToOne
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;
}
