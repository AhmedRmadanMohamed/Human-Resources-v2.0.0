package com.ahmedramadan.hr.api.dto;

import java.time.Instant;

public record TokenResponse(
        String accessToken,
        String tokenType,
        Instant expiresAt
) {
}
