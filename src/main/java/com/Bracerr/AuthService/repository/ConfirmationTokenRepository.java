package com.Bracerr.AuthService.repository;

import com.Bracerr.AuthService.models.ConfirmationToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);

    @Transactional
    @Modifying
    @Query("DELETE FROM ConfirmationToken c WHERE c.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
