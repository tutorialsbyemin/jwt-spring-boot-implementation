package com.template.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginRequestDto {
    private final String username;
    private final String password;
    private final Boolean rememberMe;
}
