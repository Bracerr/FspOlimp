package com.Bracerr.AuthService.services;

import com.Bracerr.AuthService.models.ConfirmationToken;
import com.Bracerr.AuthService.repository.ConfirmationTokenRepository;
import com.Bracerr.AuthService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository, UserRepository userRepository){
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userRepository = userRepository;
    }

    public void removeExpiredTokens() {
        List<ConfirmationToken> expiredTokens = confirmationTokenRepository.findAllByExpiryDateBefore(new Date());
        confirmationTokenRepository.deleteAll(expiredTokens);

        for(ConfirmationToken tokens: expiredTokens){
            userRepository.deleteUserRolesById(tokens.getUser().getId());
        }
    }
}
