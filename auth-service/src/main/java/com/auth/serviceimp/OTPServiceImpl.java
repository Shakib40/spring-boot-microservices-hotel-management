package com.auth.serviceimp;

import com.auth.entity.OTP;
import com.auth.repository.OTPRepository;
import com.auth.service.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {

    private final OTPRepository otpRepository;

    @Override
    public OTP createOTP(OTP otp) {
        return otpRepository.save(otp);
    }

    @Override
    public Optional<OTP> getOTPById(String id) {
        return otpRepository.findById(id);
    }

    @Override
    public Optional<OTP> getOTPByOtpAndUserName(String otp, String userName) {
        return otpRepository.findByOtpAndUserName(otp, userName);
    }

    @Override
    public List<OTP> getAllOTPs() {
        return otpRepository.findAll();
    }

    @Override
    public void deleteOTP(String id) {
        otpRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteOTPByUserName(String userName) {
        otpRepository.deleteByUserName(userName);
    }
}
