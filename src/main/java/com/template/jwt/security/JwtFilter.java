package com.template.jwt.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final MUserDetailsService mUserDetailsService;

    private Optional<String> extractToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(Const.HEADER))
                .filter(header -> header.startsWith(Const.PREFIX))
                .map(header -> header.substring(Const.PREFIX.length()));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) {

        try {
            extractToken(request)
                    .flatMap(jwtService::parseTokenToClaims)
                    .map(jwtService::getSubjectFromClaims)
                    .map(Long::parseLong)
                    .map(mUserDetailsService::loadUserById)
                    .map(ud -> new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities()))
                    .ifPresent(auth -> {
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    });

            chain.doFilter(request, response);

        } catch (Exception exception) {
            exception.printStackTrace();
            logger.error(exception.getMessage());
        }
    }

}
