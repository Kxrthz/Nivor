package com.nivor.config;

import com.nivor.security.JwtAuthenticationFilter;
import com.nivor.security.SecurityExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration @EnableWebSecurity
public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean DaoAuthenticationProvider authenticationProvider(UserDetailsService users, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(users); provider.setPasswordEncoder(encoder); return provider;
    }
    @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { return config.getAuthenticationManager(); }
    @Bean SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwt, SecurityExceptionHandler errors, DaoAuthenticationProvider provider) throws Exception {
        return http.csrf(csrf -> csrf.disable()).cors(cors -> { }).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authenticationProvider(provider).exceptionHandling(e -> e.authenticationEntryPoint(errors)).authorizeHttpRequests(a -> a.requestMatchers("/api/health", "/api/auth/register", "/api/auth/login").permitAll().anyRequest().authenticated()).addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class).build();
    }
}
