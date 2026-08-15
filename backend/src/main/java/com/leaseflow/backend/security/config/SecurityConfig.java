package com.leaseflow.backend.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.leaseflow.backend.security.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

            .csrf(AbstractHttpConfigurer::disable)

            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth

                    .requestMatchers("/api/auth/**")
                    .permitAll()

                    .anyRequest()
                    .authenticated())

            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

// What happens now? 
// Login
// POST /api/auth/login  -> return jwt 

// Next request 
// GET /api/dashboard
// Authorisation: Bearer eyJhbGC...
// -> JwtAuthentication filter -> validate token -> extract email -> then CustomUserDetailsService
// -> load user -> SecurityContextHolder -> controller
// Next step is Spring Security integration and test authentication flow 

