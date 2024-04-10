package com.Bracerr.AuthService.controllers;

import com.Bracerr.AuthService.payload.request.NewPasswordRequest;
import com.Bracerr.AuthService.services.PasswordRecoveryTokenService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Контроллер восстановления пароля", description = "Rest контроллер который создает токен восстановления. Записывает его в бд и валидирует.")
@Controller
public class RecoveryPasswordController {

    private final PasswordRecoveryTokenService passwordRecoveryTokenService;

    @Autowired
    public RecoveryPasswordController(PasswordRecoveryTokenService passwordRecoveryTokenService) {
        this.passwordRecoveryTokenService = passwordRecoveryTokenService;
    }

    @Hidden
    @RequestMapping(value = "/recovery-password-home", method = {RequestMethod.GET, RequestMethod.POST})
    public String recoveryPasswordHome(){
        return "recoveryPasswordHome";
    }

    @Operation(
            summary = "Изменение пароля пользователя",
            description = "Отправляет новый пароль в бд после всех валидаций"
    )
    @PostMapping("/api/finalRecovery")
    public ResponseEntity<?> finalRecovery (@RequestParam("token") String token, @RequestBody NewPasswordRequest newPasswordRequest){
        return passwordRecoveryTokenService.finalRecovery(token, newPasswordRequest);
    }

    @Operation(
            summary = "Работа с токеном",
            description = "Валидирует токен, создает его и записывает в бд. GET REQUEST - MVC Контроллер")
    @RequestMapping(value = "/recovery-password", method = {RequestMethod.GET, RequestMethod.POST})
    public String recoveryPassword(@RequestParam("token") String token){
        return passwordRecoveryTokenService.recoveryPassword(token);
    }

}
