package com.Bracerr.AuthService.controllers;

import com.Bracerr.AuthService.models.User;
import com.Bracerr.AuthService.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Rest контроллер users", description = "Rest контроллер получения и удаления user. Доступен только при role admin or moderator")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @Operation(
            summary = "Полечучение пользователя по email",
            description = "Позволяет получить пользователя. В заголовке должен быть передан jwtToken с role admin or moderator"
    )
    @GetMapping()
    public ResponseEntity<User> getByEmail(@RequestParam String email){
        return userService.getByEmail(email);
    }

    @Operation(
            summary = "Удаление пользователя по email",
            description = "Позволяет удалить пользователя. В заголовке должен быть передан jwtToken с role admin or moderator"
    )
    @DeleteMapping()
    public ResponseEntity<?> deleteByEmail(@RequestParam String email){
        return userService.deleteByEmail(email);
    }

}