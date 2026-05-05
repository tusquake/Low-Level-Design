package com.amazon.locker.service;

import com.amazon.locker.strategy.OTPGenerationStrategy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OTPService {
    private final OTPGenerationStrategy generationStrategy;
    private final Map<String, String> packageToOtpMap = new ConcurrentHashMap<>();
    private final Map<String, String> otpToPackageMap = new ConcurrentHashMap<>();

    public OTPService(OTPGenerationStrategy generationStrategy) {
        this.generationStrategy = generationStrategy;
    }

    public String generateOTP(String packageId) {
        String otp = generationStrategy.generateOTP();
        packageToOtpMap.put(packageId, otp);
        otpToPackageMap.put(otp, packageId);
        return otp;
    }

    public String getPackageIdByOtp(String otp) {
        return otpToPackageMap.get(otp);
    }

    public void invalidateOTP(String otp) {
        String packageId = otpToPackageMap.remove(otp);
        if (packageId != null) {
            packageToOtpMap.remove(packageId);
        }
    }
}
