package com.auth.repository;

import com.auth.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, String> {
    Optional<OTP> findByOtpAndUserName(String otp, String userName);

    void deleteByUserName(String userName);
}
