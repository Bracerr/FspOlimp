package com.Bracerr.AuthService.services;

import com.Bracerr.AuthService.models.ConfirmationToken;
import com.Bracerr.AuthService.models.PasswordRecoveryToken;
import com.Bracerr.AuthService.models.User;
import com.Bracerr.AuthService.payload.request.NewPasswordRequest;
import com.Bracerr.AuthService.repository.PasswordRecoveryTokenRepository;
import com.Bracerr.AuthService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Service
public class PasswordRecoveryTokenService {

    private final PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    @Autowired
    public PasswordRecoveryTokenService(PasswordRecoveryTokenRepository passwordRecoveryTokenRepository,
                                        PasswordEncoder passwordEncoder, UserRepository userRepository){
        this.passwordRecoveryTokenRepository = passwordRecoveryTokenRepository;
        this.encoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> finalRecovery(String token,NewPasswordRequest newPasswordRequest){
        if (passwordRecoveryTokenRepository.findByPasswordRecoveryToken(token).isEmpty()){
            return ResponseEntity.badRequest().body("invalid token");
        }
        PasswordRecoveryToken passwordRecoveryToken = passwordRecoveryTokenRepository.findByPasswordRecoveryToken(token).get();

        if (isTokenExpired(passwordRecoveryToken)){
            return ResponseEntity.badRequest().body("invalid token");
        }

        User user = passwordRecoveryTokenRepository.findUserByPasswordRecoveryToken(passwordRecoveryToken.getPasswordRecoveryToken());
        user.setPassword(encoder.encode(newPasswordRequest.getNewPassword()));
        userRepository.save(user);
        passwordRecoveryTokenRepository.delete(passwordRecoveryToken);
        return ResponseEntity.ok().body("success");
    }

    public String recoveryPassword(String token){
        if (passwordRecoveryTokenRepository.findByPasswordRecoveryToken(token).isEmpty()){
            return "errorToken";
        }
        if (isTokenExpired(passwordRecoveryTokenRepository.findByPasswordRecoveryToken(token).get())){
            return "errorToken";
        }
        return "recoveryPassword";
    }

    public void removeExpiredTokens() {
        List<PasswordRecoveryToken> expiredTokens = passwordRecoveryTokenRepository.findAllByExpiryDateBefore(new Date());
        passwordRecoveryTokenRepository.deleteAll(expiredTokens);
    }

    private boolean isTokenExpired(PasswordRecoveryToken token) {
        Date expiryDate = token.getExpiryDate();
        Date currentDate = new Date();
        return currentDate.after(expiryDate);
    }

}
