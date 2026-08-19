package com.ahmedramadan.hr.api;

import com.ahmedramadan.hr.api.dto.EmployerResponse;
import com.ahmedramadan.hr.api.dto.JobSeekerResponse;
import com.ahmedramadan.hr.api.dto.UserReportResponse;
import com.ahmedramadan.hr.api.dto.UserResponse;
import com.ahmedramadan.hr.domain.EmployerProfile;
import com.ahmedramadan.hr.domain.JobSeekerProfile;
import com.ahmedramadan.hr.domain.UserAccount;

public final class ApiMapper {

    private ApiMapper() {
    }

    public static UserResponse toUserResponse(UserAccount user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getCreatedAt(),
                user.isActive(),
                user.isActivated(),
                user.getRole().getName()
        );
    }

    public static EmployerResponse toEmployerResponse(EmployerProfile employer) {
        return new EmployerResponse(
                employer.getId(),
                employer.getCompanyName(),
                employer.getPhoneNumber(),
                employer.getWebsiteUrl()
        );
    }

    public static JobSeekerResponse toJobSeekerResponse(JobSeekerProfile jobSeeker) {
        return new JobSeekerResponse(
                jobSeeker.getId(),
                jobSeeker.getFirstName(),
                jobSeeker.getLastName(),
                jobSeeker.getBirthDate(),
                jobSeeker.getGithubUrl(),
                jobSeeker.getLinkedinUrl(),
                jobSeeker.getSummary(),
                jobSeeker.getDesiredPosition() == null ? null : jobSeeker.getDesiredPosition().getPositionName(),
                jobSeeker.getCurrentEmployer() == null ? null : jobSeeker.getCurrentEmployer().getCompanyName()
        );
    }

    public static UserReportResponse toUserReportResponse(JobSeekerProfile jobSeeker) {
        EmployerProfile employer = jobSeeker.getCurrentEmployer();
        return new UserReportResponse(
                jobSeeker.getFirstName(),
                jobSeeker.getLastName(),
                jobSeeker.getBirthDate(),
                jobSeeker.getDesiredPosition() == null ? null : jobSeeker.getDesiredPosition().getPositionName(),
                jobSeeker.getGithubUrl(),
                jobSeeker.getLinkedinUrl(),
                jobSeeker.getUser().getEmail(),
                jobSeeker.getUser().isActive(),
                jobSeeker.getUser().getCreatedAt(),
                employer == null ? null : employer.getCompanyName(),
                employer == null ? null : employer.getWebsiteUrl(),
                employer == null ? null : employer.getPhoneNumber()
        );
    }
}
