package com.abbos.moviego.entity.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-02
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@SuperBuilder
@SoftDelete(columnName = "active", strategy = SoftDeleteType.ACTIVE) // >= V 6.4
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault("NOW()")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // removed @CreatedAt
    // added PrePersist because of found problem with
    // sign up processes when calling session user!
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}

