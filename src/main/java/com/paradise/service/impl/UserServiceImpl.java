package com.paradise.service.impl;

import com.paradise.domain.entities.User;
import com.paradise.exceptions.UserAlreadyExistsException;
import com.paradise.repository.UserRepository;
import com.paradise.dto.SignUpRequest;
import com.paradise.domain.UserRole;
import com.paradise.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(SignUpRequest signUpRequest) {
        log.info("An attempt to register a new user {}", signUpRequest);
        if (userRepository.existsByLogin(signUpRequest.login())){
            throw new UserAlreadyExistsException("Username already exists with login: %s".formatted(signUpRequest.login()));
        }
        var hashedPassword = passwordEncoder.encode(signUpRequest.password());
        User userToSave = new User(
                null,
                signUpRequest.login(),
                hashedPassword,
                UserRole.USER.name()
        );
        return userRepository.save(userToSave);
    }

    public User getByLogin(String loginFromToken) {
        log.info("An attempt to find a user by login {}", loginFromToken);
        return userRepository.findByLogin(loginFromToken)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User getLocationById(Long id) {
        log.info("An attempt to get a location by id {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

    }

}
