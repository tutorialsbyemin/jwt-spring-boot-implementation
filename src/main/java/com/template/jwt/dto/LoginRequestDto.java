package com.template.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoginRequestDto {
    private final String username;
    private final String password;
    private final Boolean rememberMe;
}
