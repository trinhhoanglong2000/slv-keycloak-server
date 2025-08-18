package com.soloval.tech.services;

public interface JPAService{
    Boolean verifyOTPVertificationCode(String realmId, String userId, String otp, String transactionId);
    Boolean createOTPVertificationCode(String userId, String phoneNumber, String otpTtl, String otp, String transactionId);
}
