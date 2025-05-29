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

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@Profile("demo")
public class DemoSecurityConfig {

    private final DemoAuthenticationFilter demoAuthenticationFilter;

    public DemoSecurityConfig(DemoAuthenticationFilter demoAuthenticationFilter) {
        this.demoAuthenticationFilter = demoAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
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
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    System.out.println("UNAUTHENTICATED ACCESS (denied): " + request.getRequestURI());
                    response.sendRedirect("/index");
                })
                .authenticationEntryPoint((request, response, authException) -> {
                    System.out.println("UNAUTHENTICATED ACCESS (entrypoint): " + request.getRequestURI());
                    response.setStatus(HttpServletResponse.SC_OK);
                })
            )
            .addFilterBefore(demoAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
