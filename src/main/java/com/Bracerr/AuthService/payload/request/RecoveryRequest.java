package com.Bracerr.AuthService.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RecoveryRequest {

    @Schema(description = "Почта, указанная при регистрации", example = "example@mail.ru")
    private String email;
}
