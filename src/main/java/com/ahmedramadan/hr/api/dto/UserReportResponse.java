package com.ahmedramadan.hr.api.dto;

import java.time.Instant;
import java.time.LocalDate;

public record UserReportResponse(
        String firstName,
        String lastName,
        LocalDate birthDate,
        String desiredPosition,
        String githubUrl,
        String linkedinUrl,
        String email,
        boolean active,
        Instant createdAt,
        String company,
        String websiteUrl,
        String phoneNumber
) {
}
