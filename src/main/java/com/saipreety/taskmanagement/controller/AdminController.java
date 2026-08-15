package com.saipreety.taskmanagement.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    @GetMapping("/dashboard")
    public String adminDashboard(){
        return "Welcome Admin! You have access to the admin dashboard.";
    }
}
