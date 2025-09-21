package com.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    // Simple test endpoint
    @GetMapping("/test")
    public String test() {
        return "✅ Auth Service is running!";
    }
}
