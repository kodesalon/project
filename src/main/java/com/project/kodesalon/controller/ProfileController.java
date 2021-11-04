package com.project.kodesalon.controller;


import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    private Environment env;

    public ProfileController(final Environment env) {
        this.env = env;
    }

    @GetMapping("/profiles")
    public String getProfile() {
        return String.join(", ", env.getActiveProfiles());
    }
}
