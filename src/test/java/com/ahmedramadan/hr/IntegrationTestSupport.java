package com.ahmedramadan.hr;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.security.SecureRandom;
import java.util.Base64;

abstract class IntegrationTestSupport {

    private static final String RANDOM_JWT_SECRET = generateJwtSecret();

    @DynamicPropertySource
    static void securityProperties(DynamicPropertyRegistry registry) {
        registry.add("app.security.jwt.secret", () -> RANDOM_JWT_SECRET);
    }

    private static String generateJwtSecret() {
        byte[] secret = new byte[32];
        new SecureRandom().nextBytes(secret);
        return Base64.getEncoder().encodeToString(secret);
    }
}
