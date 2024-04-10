package com.Bracerr.AuthService.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Rest контроллер проверки jwtTokens", description = "Контроллер проверяет валидность jwt токена после авторизации, т.к оьправка заголовков недоступна при редиректе")

@RestController
@RequestMapping("/api/jwtCheck")
public class JwtCheckController {

    @Operation(
            summary = "Проверка токена",
            description = "Валидирует отправленный токен"
    )
    @GetMapping()
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public void checkToken(){
    }

}
