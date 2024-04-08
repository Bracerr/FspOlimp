package com.Bracerr.AuthService.services;

import com.Bracerr.AuthService.models.ConfirmationToken;
import com.Bracerr.AuthService.models.User;
import com.Bracerr.AuthService.payload.response.MessageResponse;
import com.Bracerr.AuthService.repository.ConfirmationTokenRepository;
import com.Bracerr.AuthService.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ConfirmEmailService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public ConfirmEmailService(ConfirmationTokenRepository confirmationTokenRepository,
                                  UserRepository userRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseEntity<?> confirmUserAccount(String confirmationToken) {

        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("invalid token."));
        }

        if (userRepository.findById(token.getUser().getId()).get().isEnabled()){
            return ResponseEntity.badRequest().body(new MessageResponse("already confirm."));
        }

        if (isTokenExpired(token)) {
            confirmationTokenRepository.deleteByUserId(token.getUser().getId());
            userRepository.deleteUserRolesById(token.getUser().getId());
            return ResponseEntity.badRequest().body(new MessageResponse("time out. retry"));
        }

        Optional<User> optionalUser = userRepository.findByEmail(token.getUser().getEmail());
        User user = optionalUser.get();
        user.setEnabled(true);
        userRepository.save(user);
        confirmationTokenRepository.deleteByUserId(user.getId());
        return ResponseEntity.ok().body(new MessageResponse("success confirm."));

    }

    private boolean isTokenExpired(ConfirmationToken token) {
        Date expiryDate = token.getExpiryDate();
        Date currentDate = new Date();
        return currentDate.after(expiryDate);
    }
}
