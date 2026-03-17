package io.hexlet.blog.controller;

import io.hexlet.blog.component.DefaultAppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
    @Value("${app.welcome-message}")
    private String welcomeMessage;
    @Autowired
    private DefaultAppProperties appInfo;

    @GetMapping("/welcome")
    public String welcome() {
        return this.appInfo.getAdminEmail() + " " + welcomeMessage;
    }
}