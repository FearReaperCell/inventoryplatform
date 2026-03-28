package com.smallbiz.inventory.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.smallbiz.inventory.service.ActivityLogService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

  private final ActivityLogService activityLogService;

  public SecurityConfig(ActivityLogService activityLogService) {
      this.activityLogService = activityLogService;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
      .authorizeHttpRequests(auth -> auth

          .requestMatchers("/login", "/css/**", "/js/**").permitAll()
          .requestMatchers("/admin/**").hasRole("ADMIN")
          .requestMatchers("/items/**", "/stock/**").hasAnyRole("ADMIN", "EMPLOYEE")
          .anyRequest().authenticated()
      )

      .formLogin(login -> login
          .loginPage("/login")
          .successHandler(successHandler())   // ✅ REPLACED THIS
          .permitAll()
      )

      .logout(logout -> logout
          .logoutSuccessUrl("/login?logout")
          .permitAll()
      )

      .csrf(csrf -> csrf.disable());

    return http.build();
  }

  // ✅ LOGIN LOGGER
  private AuthenticationSuccessHandler successHandler() {
    return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {

        String username = authentication.getName();

        activityLogService.log("LOGIN", username, "User logged in");

        response.sendRedirect("/"); // redirect after login
    };
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
  }
}