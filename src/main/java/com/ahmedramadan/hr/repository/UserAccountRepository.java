package com.ahmedramadan.hr.repository;

import com.ahmedramadan.hr.domain.RoleName;
import com.ahmedramadan.hr.domain.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    @EntityGraph(attributePaths = "role")
    Optional<UserAccount> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    @Override
    @EntityGraph(attributePaths = "role")
    Page<UserAccount> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "role")
    Page<UserAccount> findByRole_Name(RoleName role, Pageable pageable);
}
