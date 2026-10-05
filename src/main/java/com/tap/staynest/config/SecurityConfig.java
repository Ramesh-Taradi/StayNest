package com.tap.staynest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tap.staynest.service.UserService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/css/**", "/js/**", "/images/**", "/assets/**", "/favicon.ico");
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            String acceptHeader = request.getHeader("Accept");
            // For REST API calls return JSON 401
            if (acceptHeader != null && acceptHeader.contains("application/json")) {
                response.setStatus(401);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Unauthorized - Please login\"}");
            } else {
                // For browser requests redirect to login page
                response.sendRedirect("/login");
            }
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            String acceptHeader = request.getHeader("Accept");
            // For REST API calls return JSON 403
            if (acceptHeader != null && acceptHeader.contains("application/json")) {
                response.setStatus(403);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Access denied - Insufficient permissions\"}");
            } else {
                // For browser requests redirect to error page
                response.sendRedirect("/error");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .authenticationProvider(authenticationProvider())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("jwt")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .authorizeHttpRequests(auth -> auth
                        // Static resources
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/assets/**", "/favicon.ico").permitAll()

                        // Public UI pages (no login required)
                        .requestMatchers("/", "/search", "/error").permitAll()
                        .requestMatchers("/pgs/**").permitAll()
                        .requestMatchers("/login", "/register", "/logout").permitAll()
                        .requestMatchers("/auth/**", "/api/auth/**").permitAll()

                        // User pages & Bookings require authentication
                        .requestMatchers("/profile", "/profile/**", "/favorites").authenticated()
                        .requestMatchers("/bookings", "/bookings/**").authenticated()
                        .requestMatchers("/pgs/*/reviews/**").authenticated()

                        // Admin API endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Protected REST API endpoints
                        .requestMatchers("/api/pgs/**", "/api/rooms/**", "/api/bookings/**", "/api/reviews/**")
                        .hasAnyRole("USER", "ADMIN")

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
