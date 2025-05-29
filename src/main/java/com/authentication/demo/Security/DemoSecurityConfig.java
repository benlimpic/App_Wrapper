package com.authentication.demo.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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

    public DemoSecurityConfig(UserRepository userRepository) {
    this.userRepository = userRepository;
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
            .csrf(csrf -> csrf.disable()) // fine for demo
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/create-collection",    // GET for form
                    "/create_collection",    // POST for form submit
                    "/profile",              // redirect destination
                    "/css/**", "/js/**", "/images/**"
                ).permitAll()
                .anyRequest().authenticated() // require auth elsewhere
            )
            .exceptionHandling(e -> e
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    System.out.println("UNAUTHENTICATED ACCESS (denied): " + request.getRequestURI());
                    response.sendRedirect("/index");
                })
                .authenticationEntryPoint((request, response, authException) -> {
                    System.out.println("UNAUTHENTICATED ACCESS (entrypoint): " + request.getRequestURI());
                    response.setStatus(HttpServletResponse.SC_OK); // don't block
                })
            );

        // Automatically authenticate demo user
        http.addFilterBefore((request, response, chain) -> {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                userRepository.findByUsername("music-man").ifPresent(demoUser -> {
                    UserDetails userDetails = User.withUsername(demoUser.getUsername())
                        .password(demoUser.getPassword())
                        .roles(demoUser.getRoles().toArray(String[]::new))
                        .build();

                    UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(auth);
                });
            }
            chain.doFilter(request, response);
        }, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
