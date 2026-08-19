package com.ahmedramadan.hr.api.dto;

public record EmployerResponse(
        Long id,
        String companyName,
        String phoneNumber,
        String websiteUrl
) {
}
