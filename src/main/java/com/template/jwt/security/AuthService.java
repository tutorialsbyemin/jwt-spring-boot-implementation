package com.template.jwt.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public Optional<String> login(String username, String password, Boolean rememberMe) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        return Optional.of(auth)
                .filter(Authentication::isAuthenticated)
                .map(authentication -> (MUserDetails) authentication.getPrincipal())
                .map(MUserDetails::getId)
                .map(id -> jwtService.generateToken(id, rememberMe))
                .map(token -> Const.PREFIX + token);
    }
}
