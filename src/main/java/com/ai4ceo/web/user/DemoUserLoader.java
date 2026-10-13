package com.ai4ceo.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoUserLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoUserLoader.class);
    private final AppUserService userService;

    public DemoUserLoader(AppUserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        userService.ensureDemoUser("Demo User", "demo@demo.com", "Demo123");
        log.info("Demo account ready: demo@demo.com / Demo123");
    }
}
