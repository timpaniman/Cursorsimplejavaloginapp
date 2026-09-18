package com.ai4ceo.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoUserLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoUserLoader.class);
    private final AppUserRepository users;
    private final AppUserService userService;

    public DemoUserLoader(AppUserRepository users, AppUserService userService) {
        this.users = users;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        if (users.existsByEmailIgnoreCase("demo@ai4ceo.test")) {
            return;
        }
        userService.register("데모 사용자", "demo@ai4ceo.test", "Demo1234!");
        log.info("데모 계정 생성: demo@ai4ceo.test / Demo1234!");
    }
}
