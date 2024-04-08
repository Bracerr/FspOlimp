package com.Bracerr.AuthService.repository;

import java.util.Optional;

import com.Bracerr.AuthService.models.ERole;
import com.Bracerr.AuthService.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
