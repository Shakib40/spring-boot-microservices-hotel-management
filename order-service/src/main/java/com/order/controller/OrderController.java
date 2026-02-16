package com.order.controller;

import java.util.Arrays;
import java.util.List;

import com.order.dto.OrderDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OrderController {

    @GetMapping("/running")
    public String testOrderService() {
        log.info("Check order service health");
        return "✅ Order Service is running!";
    }

    @GetMapping("/orders")
    public List<OrderDto> listOrders() {
        log.info("Received request to list all orders");
        List<OrderDto> orders = Arrays.asList(
                new OrderDto("ORD-1001", "Deluxe Room", 1, "CONFIRMED"),
                new OrderDto("ORD-1002", "Suite", 2, "PENDING"),
                new OrderDto("ORD-1003", "Breakfast Add-on", 3, "CANCELLED"));
        log.info("Returning {} orders", orders.size());
        return orders;
    }
}
