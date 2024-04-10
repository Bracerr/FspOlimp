package com.Bracerr.AuthService.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Rest контроллер получение данных", description = "Rest контроллер получения данных при авторизации")

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Operation(
            summary = "Получение общих данных",
            description = "Позволяет получить общие данные для всех, даже не авторизированных пользователей"
    )
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @Operation(
            summary = "Получение user данных",
            description = "Позволяет получить данные для пользователя авторизированного с role user. В заголовке должен передаваться jwtToken с данной ролью"
    )
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @Operation(
            summary = "Получение mod данных",
            description = "Позволяет получить данные для пользователя авторизированного с role mod. В заголовке должен передаваться jwtToken с данной ролью"
    )
    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @Operation(
            summary = "Получение admin данных",
            description = "Позволяет получить данные для пользователя авторизированного с role admin. В заголовке должен передаваться jwtToken с данной ролью"
    )
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}
