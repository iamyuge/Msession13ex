package com.example.identityservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

    @GetMapping("/api/products")
    public List<Map<String, Object>> getProducts() {
        return List.of(
                Map.of(
                        "id", 1,
                        "name", "Laptop",
                        "price", 15000000
                ),
                Map.of(
                        "id", 2,
                        "name", "Keyboard",
                        "price", 500000
                ),
                Map.of(
                        "id", 3,
                        "name", "Mouse",
                        "price", 250000
                )
        );
    }
}