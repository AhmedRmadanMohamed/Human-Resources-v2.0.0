package com.ahmedramadan.hr;

import com.ahmedramadan.hr.api.dto.CreateUserRequest;
import com.ahmedramadan.hr.api.dto.LoginRequest;
import com.ahmedramadan.hr.domain.RoleName;
import com.ahmedramadan.hr.domain.SystemRole;
import com.ahmedramadan.hr.domain.UserAccount;
import com.ahmedramadan.hr.repository.SystemRoleRepository;
import com.ahmedramadan.hr.repository.UserAccountRepository;
import com.ahmedramadan.hr.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ApiSecurityIntegrationTest extends IntegrationTestSupport {

    private static final String ADMIN_EMAIL = "admin@example.test";
    private static final String ADMIN_PASSWORD = "Strong-Test-Password-123!";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    SystemRoleRepository systemRoleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenService tokenService;

    private UserAccount admin;

    @BeforeEach
    void createAdministrator() {
        SystemRole adminRole = systemRoleRepository.findByName(RoleName.ADMIN).orElseThrow();
        admin = userAccountRepository.save(UserAccount.builder()
                .email(ADMIN_EMAIL)
                .passwordHash(passwordEncoder.encode(ADMIN_PASSWORD))
                .active(true)
                .activated(true)
                .role(adminRole)
                .build());
    }

    @Test
    void protectedEndpointRejectsAnonymousRequests() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Authentication is required"));
    }

    @Test
    void openApiDocumentIsPublicAndGenerated() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("Human Resources API"))
                .andExpect(jsonPath("$.info.version").value("3.0.0"))
                .andExpect(jsonPath("$.paths['/api/v1/auth/login']").exists());
    }

    @Test
    void validCredentialsReturnShortLivedJwt() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LoginRequest(ADMIN_EMAIL, ADMIN_PASSWORD))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresAt").isNotEmpty());
    }

    @Test
    void invalidCredentialsReturnSanitizedUnauthorizedResponse() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                new LoginRequest(ADMIN_EMAIL, "Wrong-Password-That-Is-Long")
                        )))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password"))
                .andExpect(jsonPath("$.violations").isEmpty());
    }

    @Test
    void administratorCanCreateHashedUserWithoutPasswordDisclosure() throws Exception {
        String token = tokenService.issue(admin).accessToken();
        CreateUserRequest request = new CreateUserRequest(
                "hr@example.test",
                "Another-Strong-Password-123!",
                RoleName.HR
        );

        mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.email").value("hr@example.test"))
                .andExpect(jsonPath("$.role").value("HR"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.passwordHash").doesNotExist());

        UserAccount created = userAccountRepository.findByEmailIgnoreCase("hr@example.test").orElseThrow();
        assertThat(created.getPasswordHash()).isNotEqualTo(request.password());
        assertThat(passwordEncoder.matches(request.password(), created.getPasswordHash())).isTrue();
    }

    @Test
    void invalidUserRequestReturnsStructuredValidationErrors() throws Exception {
        String token = tokenService.issue(admin).accessToken();
        CreateUserRequest request = new CreateUserRequest("invalid", "short", null);

        mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.violations.email").exists())
                .andExpect(jsonPath("$.violations.password").exists())
                .andExpect(jsonPath("$.violations.role").exists());
    }

    @Test
    void nonAdministratorCannotCreateUsers() throws Exception {
        SystemRole hrRole = systemRoleRepository.findByName(RoleName.HR).orElseThrow();
        UserAccount hrUser = userAccountRepository.save(UserAccount.builder()
                .email("restricted-hr@example.test")
                .passwordHash(passwordEncoder.encode("Restricted-HR-Password-123!"))
                .active(true)
                .activated(true)
                .role(hrRole)
                .build());
        String token = tokenService.issue(hrUser).accessToken();

        mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CreateUserRequest(
                                "blocked@example.test",
                                "Blocked-User-Password-123!",
                                RoleName.JOB_SEEKER
                        ))))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("You do not have permission to perform this action"));
    }

    @Test
    void malformedRoleReturnsBadRequestWithoutInternalDetails() throws Exception {
        String token = tokenService.issue(admin).accessToken();
        String request = """
                {
                  "email": "new-user@example.test",
                  "password": "Strong-New-Password-123!",
                  "role": "ROOT"
                }
                """;

        mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The request contains an invalid value"));
    }

    @Test
    void paginationSizeIsCappedAtOneHundred() throws Exception {
        String token = tokenService.issue(admin).accessToken();

        mockMvc.perform(get("/api/v1/users?size=1000")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(100));
    }
}
