package com.Bracerr.AuthService.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

  @Schema(description = "Почта, указанная при регистрации", example = "example@mail.ru")
  @NotBlank
  private String email;

  @Schema(description = "Пароль, указанный при регистрации", example = "123456789")
  @NotBlank
  private String password;
}
