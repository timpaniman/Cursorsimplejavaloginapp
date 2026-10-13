package com.ai4ceo.web.user;

import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = users.findByEmailIgnoreCase(username.trim())
                .orElseThrow(() -> new UsernameNotFoundException("Account not found."));
        return new User(
                user.getEmail(),
                user.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Transactional
    public AppUser register(String displayName, String email, String rawPassword) {
        String normalized = email.trim().toLowerCase();
        if (users.existsByEmailIgnoreCase(normalized)) {
            throw new IllegalArgumentException("This email is already registered.");
        }
        AppUser user = new AppUser();
        user.setDisplayName(displayName.trim());
        user.setEmail(normalized);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        return users.save(user);
    }

    @Transactional
    public void ensureDemoUser(String displayName, String email, String rawPassword) {
        String normalized = email.trim().toLowerCase();
        AppUser user = users.findByEmailIgnoreCase(normalized).orElse(null);
        if (user == null) {
            user = users.findByEmailIgnoreCase("demo@ai4ceo.test").orElse(null);
        }
        if (user == null) {
            register(displayName, normalized, rawPassword);
            return;
        }
        user.setEmail(normalized);
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            user.setPasswordHash(passwordEncoder.encode(rawPassword));
        }
        users.save(user);
    }

    public AppUser requireByEmail(String email) {
        return users.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found."));
    }
}
