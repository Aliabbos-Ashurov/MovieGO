package com.abbos.moviego.entity;

import com.abbos.moviego.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Image extends BaseEntity {

    @Column(name = "generated_name", nullable = false, unique = true)
    private String generatedName;

    @NotBlank(message = "File name is required")
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @NotBlank
    @Size(max = 4, message = "Extension type must be max: {max} chars")
    @Column(nullable = false)
    private String extension;

    @Positive
    @Column(name = "size", nullable = false)
    private long size;

    @Column(name = "link", nullable = false, unique = true)
    private String link;
}
