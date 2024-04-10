package com.Bracerr.AuthService.payload.request;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignupRequest {
  @Schema(description = "Имя", example = "Иван")
  @NotBlank
  private String firstName;

  @Schema(description = "Фамилия", example = "Иванов")
  @NotBlank
  private String lastName;

  @Schema(description = "Отчество", example = "Иванович")
  @NotBlank
  private String patronymic;

  @Schema(description = "Почта", example = "example@mail.ru")
  @NotBlank
  @Email
  private String email;

  @Schema(description = "Роль. По умолчанию user, но также можно передать иные", example = "[\"admin\"]")
  private Set<String> role;

  @Schema(description = "Пароль, записывается в бд зашифровано", example = "123456789")
  @NotBlank
  private String password;

  @Schema(description = "Повторение пароля", example = "123456789")
  @NotBlank
  private String repeatPassword;
}
