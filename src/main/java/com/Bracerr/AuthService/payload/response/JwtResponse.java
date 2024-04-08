package com.Bracerr.AuthService.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String firstName;
  private String lastName;
  private String patronymic;
  private String email;
  private List<String> roles;

  public JwtResponse(String accessToken, Long id, String firstName, String lastName, String patronymic, String email, List<String> roles) {
    this.token = accessToken;
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.patronymic = patronymic;
    this.email = email;
    this.roles = roles;
  }
}
