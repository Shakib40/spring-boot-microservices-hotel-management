package com.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    // Simple test endpoint
    @GetMapping("/test")
    public String test() {
        return "✅ User Service is running!";
    }
}
