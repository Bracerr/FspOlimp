package com.Bracerr.AuthService.services;

import com.Bracerr.AuthService.models.User;
import com.Bracerr.AuthService.payload.response.MessageResponse;
import com.Bracerr.AuthService.repository.ConfirmationTokenRepository;
import com.Bracerr.AuthService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public UserService(UserRepository userRepository, ConfirmationTokenRepository confirmationTokenRepository){
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public ResponseEntity<User> getByEmail(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public ResponseEntity<?> deleteByEmail(String email){
        if(confirmationTokenRepository.existsByUserId(userRepository.findByEmail(email).get().getId())){

            confirmationTokenRepository.deleteByUserId(userRepository.findByEmail(email).get().getId());
            userRepository.deleteUserRolesById(userRepository.findByEmail(email).get().getId());
            return ResponseEntity.ok().body(new MessageResponse("Success delete."));
        }

        userRepository.deleteUserRolesById(userRepository.findByEmail(email).get().getId());
        return ResponseEntity.ok().body(new MessageResponse("Success delete."));
    }

}
