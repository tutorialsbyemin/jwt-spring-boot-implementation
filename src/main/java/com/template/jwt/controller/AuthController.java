package com.template.jwt.controller;

import com.template.jwt.dto.LoginRequestDto;
import com.template.jwt.exception.InvalidTokenGenerationException;
import com.template.jwt.security.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto payload) {
        return ResponseEntity.ok(
                authService.login(payload.getUsername(), payload.getPassword(), payload.getRememberMe())
                        .orElseThrow(InvalidTokenGenerationException::new));
    }
}
