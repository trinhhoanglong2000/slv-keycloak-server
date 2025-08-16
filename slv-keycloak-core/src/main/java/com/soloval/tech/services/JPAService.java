package com.soloval.tech.services;
import org.keycloak.provider.Provider;

public interface JPAService{

    Boolean createOTPVertificationCode(String userId, String phoneNumber, String otpTtl, String otp, String transactionId);
}
