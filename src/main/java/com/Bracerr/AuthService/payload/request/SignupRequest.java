package com.Bracerr.AuthService.payload.request;

import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignupRequest {
  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @NotBlank
  private String patronymic;

  @NotBlank
  @Email
  private String email;

  private Set<String> role;

  @NotBlank
  private String password;

  @NotBlank
  private String repeatPassword;
}
