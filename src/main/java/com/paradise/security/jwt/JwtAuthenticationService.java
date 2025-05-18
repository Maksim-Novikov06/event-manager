package com.paradise.security.jwt;

import com.paradise.entities.User;
import com.paradise.repository.UserRepository;
import com.paradise.security.SignInRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager jwtTokenManager;
    private final UserRepository userRepository;

    public String authenticateUser(SignInRequest signInRequest) {
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
