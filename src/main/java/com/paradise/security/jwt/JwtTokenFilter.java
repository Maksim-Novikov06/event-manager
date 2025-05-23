package com.paradise.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenManager jwtTokenManager;

    public JwtTokenFilter(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    )throws ServletException, IOException
    {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authorizationHeader.substring(7);

        String loginFromToken;
        Long userId;

        try {
            loginFromToken = jwtTokenManager.getLoginFromToken(jwtToken);
            userId = jwtTokenManager.getUserIdFromToken(jwtToken);

            CustomPrincipal customPrincipal = new CustomPrincipal(userId, loginFromToken);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            customPrincipal,
                            loginFromToken,
                            List.of(new SimpleGrantedAuthority("ADMIN"),
                                    new SimpleGrantedAuthority("USER"))
                    );
            authentication.setDetails(userId);

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        } catch (Exception e) {
           logger.error("Invalid JWT Token");
           filterChain.doFilter(request, response);
           return;
        }



        filterChain.doFilter(request, response);

    }
}
