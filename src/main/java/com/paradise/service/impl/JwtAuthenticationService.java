package com.paradise.service.impl;

import com.paradise.domain.entities.User;
import com.paradise.repository.UserRepository;
import com.paradise.dto.SignInRequest;
import com.paradise.security.jwt.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationService.class);
    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager jwtTokenManager;
    private final UserRepository userRepository;

    public String authenticateUser(SignInRequest signInRequest) {
        logger.info("Attempt to authenticate user: {}", signInRequest);
        User user = userRepository.findByLogin(signInRequest.login())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + signInRequest.login()));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.login(),
                        signInRequest.password()
                )
        );

        return jwtTokenManager.generateToken(signInRequest.login());
    }
}
