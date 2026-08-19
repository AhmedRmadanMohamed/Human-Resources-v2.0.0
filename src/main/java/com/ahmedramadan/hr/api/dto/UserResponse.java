package com.ahmedramadan.hr.api.dto;

import com.ahmedramadan.hr.domain.RoleName;

import java.time.Instant;

public record UserResponse(
        Long id,
        String email,
        Instant createdAt,
        boolean active,
        boolean activated,
        RoleName role
) {
}
