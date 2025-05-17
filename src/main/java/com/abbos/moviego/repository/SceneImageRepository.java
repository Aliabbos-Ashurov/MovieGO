package com.abbos.moviego.repository;

import com.abbos.moviego.entity.SceneImage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public interface SceneImageRepository extends ListCrudRepository<SceneImage, Long> {

    @Query("SELECT si FROM SceneImage si JOIN FETCH si.image i WHERE si.movie.id = :movieId")
    List<SceneImage> findSceneImagesByMovieId(Long movieId);

    @Query("""
                SELECT si.image.link
                FROM SceneImage si
                WHERE si.movie.id = :movieId
            """)
    List<String> findSceneImagesLink(Long movieId);
}
