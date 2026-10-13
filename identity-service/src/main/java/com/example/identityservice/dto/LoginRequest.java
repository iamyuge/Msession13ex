package com.example.identityservice.dto;

public record LoginRequest(
        String username,
        String password
) {
}