package com.abbos.moviego.repository;

import com.abbos.moviego.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM Image i WHERE i.mimeType = :mimeType")
    List<Image> findImagesByMimeType(String mimeType);

}
