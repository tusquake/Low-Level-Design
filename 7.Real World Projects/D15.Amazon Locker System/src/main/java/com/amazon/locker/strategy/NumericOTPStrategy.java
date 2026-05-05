package com.amazon.locker.strategy;

import java.util.Random;

public class NumericOTPStrategy implements OTPGenerationStrategy {
    private static final int OTP_LENGTH = 6;
    private final Random random = new Random();

    @Override
    public String generateOTP() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
