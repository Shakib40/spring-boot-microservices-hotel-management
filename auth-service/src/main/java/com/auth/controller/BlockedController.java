package com.auth.controller;

import com.auth.entity.blocked;
import com.auth.service.BlockedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blocked")
@RequiredArgsConstructor
public class BlockedController {

    private final BlockedService blockedService;

    @PostMapping
    public ResponseEntity<blocked> blockUser(@RequestBody blocked blockedUser) {
        return ResponseEntity.ok(blockedService.blockUser(blockedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<blocked> getBlockedUserById(@PathVariable String id) {
        return blockedService.getBlockedUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userName}")
    public ResponseEntity<blocked> getBlockedUserByUserName(@PathVariable String userName) {
        return blockedService.getBlockedUserByUserName(userName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<blocked>> getAllBlockedUsers() {
        return ResponseEntity.ok(blockedService.getAllBlockedUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unblockUser(@PathVariable String id) {
        blockedService.unblockUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userName}")
    public ResponseEntity<Void> unblockUserByUserName(@PathVariable String userName) {
        blockedService.unblockUserByUserName(userName);
        return ResponseEntity.noContent().build();
    }
}
