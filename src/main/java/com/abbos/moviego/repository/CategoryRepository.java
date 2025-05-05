package com.abbos.moviego.repository;

import com.abbos.moviego.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.name = :name")
    Optional<Category> findCategoryByName(String name);
}
