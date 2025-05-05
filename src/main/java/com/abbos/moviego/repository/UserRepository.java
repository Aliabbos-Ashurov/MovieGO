package com.abbos.moviego.repository;

import com.abbos.moviego.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.fullname LIKE %:name%")
    List<User> findUsersByNameLike(String name);
}
