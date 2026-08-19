package com.ahmedramadan.hr.service;

import com.ahmedramadan.hr.api.ApiMapper;
import com.ahmedramadan.hr.api.dto.EmployerResponse;
import com.ahmedramadan.hr.api.dto.JobSeekerResponse;
import com.ahmedramadan.hr.api.dto.PageResponse;
import com.ahmedramadan.hr.api.dto.UserReportResponse;
import com.ahmedramadan.hr.domain.RoleName;
import com.ahmedramadan.hr.repository.EmployerProfileRepository;
import com.ahmedramadan.hr.repository.JobSeekerProfileRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DirectoryService {

    private final EmployerProfileRepository employerProfileRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;

    public DirectoryService(
            EmployerProfileRepository employerProfileRepository,
            JobSeekerProfileRepository jobSeekerProfileRepository
    ) {
        this.employerProfileRepository = employerProfileRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<EmployerResponse> listEmployers(Pageable pageable) {
        return PageResponse.from(employerProfileRepository.findAll(pageable), ApiMapper::toEmployerResponse);
    }

    @Transactional(readOnly = true)
    public PageResponse<JobSeekerResponse> listJobSeekers(Pageable pageable) {
        return PageResponse.from(jobSeekerProfileRepository.findAll(pageable), ApiMapper::toJobSeekerResponse);
    }

    @Transactional(readOnly = true)
    public PageResponse<JobSeekerResponse> listJobSeekersByPosition(String position, Pageable pageable) {
        return PageResponse.from(
                jobSeekerProfileRepository.findByDesiredPosition_PositionNameIgnoreCase(position, pageable),
                ApiMapper::toJobSeekerResponse
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<UserReportResponse> userReport(RoleName role, Pageable pageable) {
        return PageResponse.from(
                jobSeekerProfileRepository.findByUser_Role_Name(role, pageable),
                ApiMapper::toUserReportResponse
        );
    }
}
