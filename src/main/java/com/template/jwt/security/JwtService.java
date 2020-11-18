package com.template.jwt.security;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Log4j2
@Service
@PropertySource("classpath:jwt.properties")
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiry.default}")
    private long expiryDefault;

    @Value("${jwt.expiry.remember}")
    private long expiryRememberMe;

    public String generateToken(long userId, boolean rememberMe) {
        final Date now = new Date();
        final long delta = rememberMe ? expiryRememberMe : expiryDefault;

        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + delta))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

    }

    public Optional<Jws<Claims>> parseTokenToClaims(String token) {
        try {
            return Optional.of(
                    Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
            );
        } catch (SignatureException ex) {
            log.error("JWT: Invalid signature");
        } catch (MalformedJwtException ex) {
            log.error("JWT: Invalid token");
        } catch (ExpiredJwtException ex) {
            log.error("JWT: Expired token");
        } catch (UnsupportedJwtException ex) {
            log.error("JWT: Unsupported token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT: token is empty.");
        }
        return Optional.empty();
    }

    public String getSubjectFromClaims(Jws<Claims> claimsJws) {
            return claimsJws.getBody().getSubject();
    }
}
