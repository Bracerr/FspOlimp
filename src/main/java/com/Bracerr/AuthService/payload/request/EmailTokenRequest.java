package com.Bracerr.AuthService.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailTokenRequest {
    @NotBlank
    private String token;
}
