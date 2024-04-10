package com.Bracerr.AuthService.repository;

import com.Bracerr.AuthService.models.PasswordRecoveryToken;
import com.Bracerr.AuthService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordRecoveryTokenRepository extends JpaRepository<PasswordRecoveryToken, Long> {

    Optional<PasswordRecoveryToken> findByPasswordRecoveryToken(String passwordRecoveryToken);

    @Query("SELECT prt.user FROM PasswordRecoveryToken prt WHERE prt.passwordRecoveryToken = :token")
    User findUserByPasswordRecoveryToken(@Param("token") String token);

    boolean existsByUserId(Long id);
    List<PasswordRecoveryToken> findAllByExpiryDateBefore(Date now);


}
