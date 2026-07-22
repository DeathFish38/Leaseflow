package com.leaseflow.backend.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.leaseflow.backend.auth.dto.RegisterRequest;
import com.leaseflow.backend.auth.dto.UserResponse;
import com.leaseflow.backend.auth.dto.LoginRequest;
import com.leaseflow.backend.auth.dto.LoginResponse;
import com.leaseflow.backend.common.exception.user.DuplicateEmailException;
import com.leaseflow.backend.common.exception.user.InvalidCredentialsException;
import com.leaseflow.backend.users.entity.User;
import com.leaseflow.backend.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // create new user
    public UserResponse register(RegisterRequest request) {

        // check duplicate email
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException(request.email());
        }

        User user = new User();
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPasswordHash(passwordEncoder.encode(request.password()));

        // save user
        User savedUser = userRepository.save(user);
        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName());

    }

    // login
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash())) {

            throw new InvalidCredentialsException();
        }

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                "Login successful");

    }

}
