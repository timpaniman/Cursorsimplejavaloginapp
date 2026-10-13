package com.ai4ceo.web.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupForm {

    @NotBlank(message = "Enter your name.")
    @Size(max = 40, message = "Name must be 40 characters or fewer.")
    private String displayName;

    @NotBlank(message = "Enter your email.")
    @Email(message = "Enter a valid email address.")
    private String email;

    @NotBlank(message = "Enter a password.")
    @Size(min = 6, max = 72, message = "Password must be at least 6 characters.")
    private String password;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
