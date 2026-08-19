package com.ahmedramadan.hr.service;

import com.ahmedramadan.hr.api.ApiMapper;
import com.ahmedramadan.hr.api.dto.CreateUserRequest;
import com.ahmedramadan.hr.api.dto.PageResponse;
import com.ahmedramadan.hr.api.dto.UserResponse;
import com.ahmedramadan.hr.domain.RoleName;
import com.ahmedramadan.hr.domain.SystemRole;
import com.ahmedramadan.hr.domain.UserAccount;
import com.ahmedramadan.hr.error.ConflictException;
import com.ahmedramadan.hr.error.ResourceNotFoundException;
import com.ahmedramadan.hr.repository.SystemRoleRepository;
import com.ahmedramadan.hr.repository.UserAccountRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class UserService {

    private final UserAccountRepository userAccountRepository;
    private final SystemRoleRepository systemRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserAccountRepository userAccountRepository,
            SystemRoleRepository systemRoleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userAccountRepository = userAccountRepository;
        this.systemRoleRepository = systemRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public PageResponse<UserResponse> list(Pageable pageable) {
        return PageResponse.from(userAccountRepository.findAll(pageable), ApiMapper::toUserResponse);
    }

    @Transactional(readOnly = true)
    public PageResponse<UserResponse> listByRole(RoleName role, Pageable pageable) {
        return PageResponse.from(userAccountRepository.findByRole_Name(role, pageable), ApiMapper::toUserResponse);
    }

    @Transactional(readOnly = true)
    public UserResponse get(Long id) {
        return userAccountRepository.findById(id)
                .map(ApiMapper::toUserResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        UserAccount account = createAccount(request.email(), request.password(), request.role());
        return ApiMapper.toUserResponse(account);
    }

    @Transactional
    public UserAccount createBootstrapAdmin(String email, String password) {
        if (password == null || password.length() < 12 || password.length() > 72) {
            throw new IllegalStateException("BOOTSTRAP_ADMIN_PASSWORD must contain between 12 and 72 characters");
        }
        return createAccount(email, password, RoleName.ADMIN);
    }

    private UserAccount createAccount(String email, String password, RoleName roleName) {
        String normalizedEmail = normalizeEmail(email);
        if (userAccountRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new ConflictException("A user with this email already exists");
        }
        SystemRole role = systemRoleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Required role is missing: " + roleName));

        UserAccount account = UserAccount.builder()
                .email(normalizedEmail)
                .passwordHash(passwordEncoder.encode(password))
                .active(true)
                .activated(true)
                .role(role)
                .build();
        return userAccountRepository.save(account);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
