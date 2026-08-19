package com.ahmedramadan.hr.config;

import com.ahmedramadan.hr.repository.UserAccountRepository;
import com.ahmedramadan.hr.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AdminBootstrapRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrapRunner.class);

    private final AdminBootstrapProperties properties;
    private final UserAccountRepository userAccountRepository;
    private final UserService userService;

    public AdminBootstrapRunner(
            AdminBootstrapProperties properties,
            UserAccountRepository userAccountRepository,
            UserService userService
    ) {
        this.properties = properties;
        this.userAccountRepository = userAccountRepository;
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) {
        boolean emailPresent = StringUtils.hasText(properties.email());
        boolean passwordPresent = StringUtils.hasText(properties.password());
        if (!emailPresent && !passwordPresent) {
            return;
        }
        if (!emailPresent || !passwordPresent) {
            throw new IllegalStateException(
                    "BOOTSTRAP_ADMIN_EMAIL and BOOTSTRAP_ADMIN_PASSWORD must be configured together"
            );
        }
        String email = properties.email().trim();
        if (userAccountRepository.existsByEmailIgnoreCase(email)) {
            log.info("Bootstrap administrator already exists");
            return;
        }
        userService.createBootstrapAdmin(email, properties.password());
        log.info("Bootstrap administrator created");
    }
}
