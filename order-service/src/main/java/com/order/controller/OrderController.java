package com.order.controller;

import java.util.Arrays;
import java.util.List;

import com.order.dto.OrderDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @GetMapping("/running")
    public String testOrderService() {
        return "✅ Order Service is running!";
    }

    @GetMapping("/orders")
    public List<OrderDto> listOrders() {
        return Arrays.asList(
                new OrderDto("ORD-1001", "Deluxe Room", 1, "CONFIRMED"),
                new OrderDto("ORD-1002", "Suite", 2, "PENDING"),
                new OrderDto("ORD-1003", "Breakfast Add-on", 3, "CANCELLED")
        );
    }
}
