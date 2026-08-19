package com.ahmedramadan.hr.repository;

import com.ahmedramadan.hr.domain.JobSeekerProfile;
import com.ahmedramadan.hr.domain.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSeekerProfileRepository extends JpaRepository<JobSeekerProfile, Long> {

    @Override
    @EntityGraph(attributePaths = {"user", "user.role", "currentEmployer", "desiredPosition"})
    Page<JobSeekerProfile> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "user.role", "currentEmployer", "desiredPosition"})
    Page<JobSeekerProfile> findByDesiredPosition_PositionNameIgnoreCase(String positionName, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "user.role", "currentEmployer", "desiredPosition"})
    Page<JobSeekerProfile> findByUser_Role_Name(RoleName role, Pageable pageable);
}
