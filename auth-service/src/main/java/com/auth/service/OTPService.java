package com.auth.service;

import com.auth.entity.OTP;
import java.util.List;
import java.util.Optional;

public interface OTPService {
    OTP createOTP(OTP otp);

    Optional<OTP> getOTPById(String id);

    Optional<OTP> getOTPByOtpAndUserName(String otp, String userName);

    List<OTP> getAllOTPs();

    void deleteOTP(String id);

    void deleteOTPByUserName(String userName);
}
