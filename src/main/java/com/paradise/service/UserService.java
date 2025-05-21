package com.paradise.service;

import com.paradise.domain.entities.User;
import com.paradise.dto.SignUpRequest;

public interface UserService {

    User registerUser(SignUpRequest signUpRequest);
    User getByLogin(String loginFromToken);
    User getLocationById(Long id);
}
