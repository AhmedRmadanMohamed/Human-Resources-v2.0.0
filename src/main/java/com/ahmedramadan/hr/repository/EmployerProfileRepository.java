package com.ahmedramadan.hr.repository;

import com.ahmedramadan.hr.domain.EmployerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, Long> {

    @Override
    @EntityGraph(attributePaths = "user")
    Page<EmployerProfile> findAll(Pageable pageable);
}
