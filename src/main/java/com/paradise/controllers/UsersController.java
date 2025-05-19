package com.paradise.controllers;

import com.paradise.converter.UserMapper;
import com.paradise.dto.UserDto;
import com.paradise.entities.User;
import com.paradise.security.SignInRequest;
import com.paradise.security.SignUpRequest;
import com.paradise.security.jwt.JwtTokenResponse;
import com.paradise.security.jwt.JwtAuthenticationService;
import com.paradise.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {

    private static final Logger log = LoggerFactory.getLogger(UsersController.class);

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtAuthenticationService jwtAuthenticationService;


    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody @Valid SignUpRequest signUpRequest
    ) {
        log.info("Get request for sign-up: login={}", signUpRequest.login());

        User user = userService.registerUser(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.toDto(user));
    }


    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticate(
            @RequestBody @Valid SignInRequest signInRequest
    ){
        log.info("Get request for sign-in: login={}", signInRequest.login());
        var token = jwtAuthenticationService.authenticateUser(signInRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new JwtTokenResponse(token));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable Long id
    ) {
        User user = userService.getLocationById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userMapper.toDto(user));
    }

}
