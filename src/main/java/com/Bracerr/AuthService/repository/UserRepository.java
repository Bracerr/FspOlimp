package com.Bracerr.AuthService.repository;

import java.util.Optional;

import com.Bracerr.AuthService.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id = ?1")
    void deleteUserRolesById(Long userId);
}
