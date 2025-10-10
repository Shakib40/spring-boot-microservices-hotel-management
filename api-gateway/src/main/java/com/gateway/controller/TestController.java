package com.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-gateway")
public class TestController {
    
    @GetMapping("/running")
    public String test() {
        return "Api Gateway is running";
    }
}
