package com.auth.controller;

import com.auth.entity.OTP;
import com.auth.service.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OTPController {

    private final OTPService otpService;

    @PostMapping
    public ResponseEntity<OTP> createOTP(@RequestBody OTP otp) {
        return ResponseEntity.ok(otpService.createOTP(otp));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OTP> getOTPById(@PathVariable String id) {
        return otpService.getOTPById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<OTP> getOTPByOtpAndUserName(@RequestParam String otp, @RequestParam String userName) {
        return otpService.getOTPByOtpAndUserName(otp, userName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OTP>> getAllOTPs() {
        return ResponseEntity.ok(otpService.getAllOTPs());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOTP(@PathVariable String id) {
        otpService.deleteOTP(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userName}")
    public ResponseEntity<Void> deleteOTPByUserName(@PathVariable String userName) {
        otpService.deleteOTPByUserName(userName);
        return ResponseEntity.noContent().build();
    }
}
