package com.ahmedramadan.hr.api.dto;

import java.time.LocalDate;

public record JobSeekerResponse(
        Long id,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String githubUrl,
        String linkedinUrl,
        String summary,
        String desiredPosition,
        String currentEmployer
) {
}
