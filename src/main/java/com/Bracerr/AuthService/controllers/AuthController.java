package com.Bracerr.AuthService.controllers;

import com.Bracerr.AuthService.models.PasswordRecoveryToken;
import com.Bracerr.AuthService.payload.request.RecoveryRequest;
import com.Bracerr.AuthService.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Bracerr.AuthService.payload.request.LoginRequest;
import com.Bracerr.AuthService.payload.request.SignupRequest;


@Tag(name = "Основной контроллер", description = "Rest контроллер авторизации, регистрации, восстановления пароля")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @Operation(
            summary = "Авторизация",
            description = "Позволяет зарегистрировать пользователя. При ответе отправляется jwtToken, который используется в дальнейшем на других страницах с доступом"
    )
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет зарегистрировать пользователя. Если ответ \"User registered successfully!\", значит письмо отправлено на почту"
    )
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    @Operation(
            summary = "Восстановление пароля",
            description = "Позволяет восстановить пароль. Отправляет письмо на почту при нахождении Email"
    )
    @PostMapping("/recovery")
    public ResponseEntity<?> recoveryPassword(@Valid @RequestBody RecoveryRequest recoveryRequest){
        return authService.recoveryPassword(recoveryRequest);
    }
}
