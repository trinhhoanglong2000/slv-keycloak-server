package com.soloval.tech.utils;

import org.keycloak.common.util.SecretGenerator;

import java.security.SecureRandom;

public class SecurityUtils {
    private static final SecureRandom secureRandom = new SecureRandom();
    public static String generateOTP(int digits) {
        SecretGenerator.getInstance().randomString(digits);
        int max = (int) Math.pow(10, digits);
        int otp = secureRandom.nextInt(max);

        return String.format("%0" + digits + "d", otp);
    }
}
