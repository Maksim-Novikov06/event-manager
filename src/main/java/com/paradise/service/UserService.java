package com.paradise.service;

import com.paradise.controllers.UsersController;
import com.paradise.entities.User;
import com.paradise.exceptions.UserAlreadyExistsException;
import com.paradise.repository.UserRepository;
import com.paradise.security.SignUpRequest;
import com.paradise.security.UserRole;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Data
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UsersController.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(SignUpRequest signUpRequest) {
        log.info("Inside registerUser");
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

    public User findByLogin(String loginFromToken) {
        log.info("Inside findByLogin");
        return userRepository.findByLogin(loginFromToken)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User getLocationById(Long id) {
        log.info("Inside getLocationById");
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

    }
}
