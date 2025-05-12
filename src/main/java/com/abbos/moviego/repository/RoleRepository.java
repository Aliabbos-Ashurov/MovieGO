package com.abbos.moviego.repository;

import com.abbos.moviego.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-09
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    Set<Role> findAllByIdIn(Set<Long> ids);

    Set<Long> id(Long id);
}
