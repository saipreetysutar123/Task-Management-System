package com.saipreety.taskmanagement.config;

import com.saipreety.taskmanagement.controller.AdminController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
@Import({
        AdminController.class,
        SecurityConfigTest.TestSecurityConfig.class
})
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void adminDashboard_WithoutAuthentication_ShouldReturn401()
            throws Exception {

        mockMvc.perform(
                        get("/admin/dashboard")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void adminDashboard_WithUserRole_ShouldReturn403()
            throws Exception {

        mockMvc.perform(
                        get("/admin/dashboard")
                                .with(user("user@gmail.com")
                                        .roles("USER"))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void adminDashboard_WithAdminRole_ShouldReturn200() throws Exception {

        mockMvc.perform(
                        get("/admin/dashboard")
                                .with(
                                        org.springframework.security.test.web.servlet.request
                                                .SecurityMockMvcRequestPostProcessors
                                                .user("admin@gmail.com")
                                                .roles("ADMIN")
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                        "Welcome Admin! You have access to the admin dashboard."
                ));
    }

    @Configuration
    @EnableWebSecurity
    static class TestSecurityConfig {

        @Bean
        SecurityFilterChain testSecurityFilterChain(
                HttpSecurity http) throws Exception {

            http
                    .csrf(csrf -> csrf.disable())
                    .exceptionHandling(exception -> exception
                            .authenticationEntryPoint(
                                    (request, response, authException) -> {
                                        response.setStatus(401);
                                    }
                            )
                    )
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/admin/**")
                            .hasRole("ADMIN")
                            .anyRequest()
                            .authenticated()
                    );

            return http.build();
        }
    }
}