package com.paradise.controllers;

import com.paradise.mapper.UserMapper;
import com.paradise.dto.UserDto;
import com.paradise.domain.entities.User;
import com.paradise.dto.SignInRequest;
import com.paradise.dto.SignUpRequest;
import com.paradise.dto.JwtTokenResponse;
import com.paradise.service.impl.JwtAuthenticationService;
import com.paradise.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {



    private final UserServiceImpl userServiceImpl;
    private final UserMapper userMapper;
    private final JwtAuthenticationService jwtAuthenticationService;


    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody @Valid SignUpRequest signUpRequest
    ) {


        User user = userServiceImpl.registerUser(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.toDto(user));
    }


    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticate(
            @RequestBody @Valid SignInRequest signInRequest
    ){

        var token = jwtAuthenticationService.authenticateUser(signInRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new JwtTokenResponse(token));
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable Long id
    ) {
        User user = userServiceImpl.getLocationById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userMapper.toDto(user));
    }

}
