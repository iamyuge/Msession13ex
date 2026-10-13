package com.example.identityservice.dto;

public record RegisterRequest(
        String username,
        String password
) {
}