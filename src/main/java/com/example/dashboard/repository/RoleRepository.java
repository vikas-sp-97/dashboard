package com.example.dashboard.repository;

import com.example.dashboard.entity.Role;
import com.example.dashboard.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRole(RoleEnum role);
}
