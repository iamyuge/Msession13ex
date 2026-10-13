package com.example.identityservice.controller;

import com.example.identityservice.dto.LoginRequest;
import com.example.identityservice.dto.RegisterRequest;
import com.example.identityservice.entity.User;
import com.example.identityservice.repository.UserRepository;
import com.example.identityservice.service.AuthService;
import com.example.identityservice.util.JwtUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(
            AuthService authService,
            UserRepository userRepository,
            JwtUtil jwtUtil) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @RequestBody RegisterRequest request) {

        User user = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "message", "Đăng ký thành công",
                        "id", user.getId().toString(),
                        "username", user.getUsername(),
                        "role", user.getRole()
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody LoginRequest request) {

        String token = authService.login(request);

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "tokenType", "Bearer"
                )
        );
    }

    // Chỉ dùng kiểm thử Bài 2; không dùng trong production.
    @GetMapping("/test-token")
    public ResponseEntity<String> testToken(
            @RequestParam String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy người dùng"
                ));

        return ResponseEntity.ok(jwtUtil.generateToken(user));
    }
}