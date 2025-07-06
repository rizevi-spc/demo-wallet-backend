package com.example.demo.repository;

import com.example.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * role jpa repository
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findRoleByName(String name);
}
