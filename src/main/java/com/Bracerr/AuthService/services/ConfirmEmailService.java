package com.Bracerr.AuthService.services;

import com.Bracerr.AuthService.models.ConfirmationToken;
import com.Bracerr.AuthService.models.User;
import com.Bracerr.AuthService.repository.ConfirmationTokenRepository;
import com.Bracerr.AuthService.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String confirmUserAccount(@RequestParam("token") String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(token);

        if (confirmationToken == null) {
            return "errorToken";
        }

        if (isTokenExpired(confirmationToken)) {
            confirmationTokenRepository.deleteByUserId(confirmationToken.getUser().getId());
            userRepository.deleteUserRolesById(confirmationToken.getUser().getId());
            return "errorToken";
        }

        Optional<User> optionalUser = userRepository.findByEmail(confirmationToken.getUser().getEmail());
        User user = optionalUser.get();
        user.setEnabled(true);
        userRepository.save(user);
        confirmationTokenRepository.deleteByUserId(user.getId());
        return "successConfirm";
    }

    private boolean isTokenExpired(ConfirmationToken token) {
        Date expiryDate = token.getExpiryDate();
        Date currentDate = new Date();
        return currentDate.after(expiryDate);
    }
}
