package com.ahmedramadan.hr.repository;

import com.ahmedramadan.hr.domain.RoleName;
import com.ahmedramadan.hr.domain.SystemRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemRoleRepository extends JpaRepository<SystemRole, Long> {

    Optional<SystemRole> findByName(RoleName name);
}
