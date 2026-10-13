package com.example.identityservice.service;

import com.example.identityservice.dto.LoginRequest;
import com.example.identityservice.dto.RegisterRequest;
import com.example.identityservice.entity.User;
import com.example.identityservice.repository.UserRepository;
import com.example.identityservice.util.JwtUtil;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public User register(RegisterRequest request) {
        if (request == null
                || request.username() == null
                || request.username().isBlank()
                || request.password() == null
                || request.password().length() < 8) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username không được để trống và mật khẩu phải có ít nhất 8 ký tự"
            );
        }

        String username = request.username().trim();

        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username đã tồn tại"
            );
        }

        User user = new User(
                username,
                passwordEncoder.encode(request.password()),
                "ROLE_USER"
        );

        return userRepository.save(user);
    }

    public String login(LoginRequest request) {
        if (request == null
                || request.username() == null
                || request.username().isBlank()
                || request.password() == null) {
            throw badCredentials();
        }

        User user = userRepository
                .findByUsername(request.username().trim())
                .orElseThrow(AuthService::badCredentials);

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword())) {
            throw badCredentials();
        }

        return jwtUtil.generateToken(user);
    }

    private static ResponseStatusException badCredentials() {
        return new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Bad credentials"
        );
    }
}