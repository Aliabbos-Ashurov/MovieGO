package com.abbos.moviego.repository;

import com.abbos.moviego.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;
import java.util.Set;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-11
 */
public interface PermissionRepository extends ListCrudRepository<Permission, Long> {

    Optional<Permission> findByName(String name);

    @Query("FROM Permission p WHERE p.id IN (:permissionIds)")
    Set<Permission> findAllByIds(Set<Long> permissionIds);
}
