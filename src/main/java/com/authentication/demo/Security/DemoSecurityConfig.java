package com.authentication.demo.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.authentication.demo.Repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@Profile("demo")
public class DemoSecurityConfig {

    private final UserRepository userRepository;
    private final DemoAuthenticationFilter demoAuthenticationFilter;

    public DemoSecurityConfig(UserRepository userRepository, DemoAuthenticationFilter demoAuthenticationFilter) {
        this.userRepository = userRepository;
        this.demoAuthenticationFilter = demoAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/create-collection",
                    "/create_collection",
                    "/profile",
                    "/css/**", "/js/**", "/images/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(e -> e
                .accessDeniedHandler((req, res, ex) -> {
                    System.out.println("Denied: " + req.getRequestURI());
                    res.sendRedirect("/index");
                })
                .authenticationEntryPoint((req, res, ex) -> {
                    System.out.println("Unauthenticated: " + req.getRequestURI());
                    res.setStatus(HttpServletResponse.SC_OK);
                })
            )
            .addFilterAfter(demoAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // <- ✅

        return http.build();
    }
}

