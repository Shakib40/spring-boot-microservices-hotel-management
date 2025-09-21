package com.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @GetMapping("/test")
    public String testOrderService() {
        return "✅ Order Service is running!";
    }
}
