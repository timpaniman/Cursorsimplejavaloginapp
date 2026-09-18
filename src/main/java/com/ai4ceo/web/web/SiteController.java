package com.ai4ceo.web.web;

import com.ai4ceo.web.user.AppUser;
import com.ai4ceo.web.user.AppUserService;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SiteController {

    private final AppUserService users;

    public SiteController(AppUserService users) {
        this.users = users;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        if (!model.containsAttribute("signupForm")) {
            model.addAttribute("signupForm", new SignupForm());
        }
        return "signup";
    }

    @PostMapping("/signup")
    public String register(
            @Valid @ModelAttribute("signupForm") SignupForm form,
            BindingResult binding,
            RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            return "signup";
        }
        try {
            users.register(form.getDisplayName(), form.getEmail(), form.getPassword());
        } catch (IllegalArgumentException ex) {
            binding.rejectValue("email", "exists", ex.getMessage());
            return "signup";
        }
        redirect.addFlashAttribute("signupSuccess", true);
        return "redirect:/login";
    }

    @GetMapping("/app")
    public String app(Principal principal, Model model) {
        AppUser user = users.requireByEmail(principal.getName());
        model.addAttribute("user", user);
        return "app";
    }
}
