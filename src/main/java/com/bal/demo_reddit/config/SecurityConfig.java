package com.bal.demo_reddit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // CSRF-Schutz deaktivieren
                .formLogin(AbstractHttpConfigurer::disable)  // Form-Login deaktivieren
                .httpBasic(AbstractHttpConfigurer::disable)  // HTTP Basic Auth deaktivieren
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("api/auth/**").permitAll()
                        .anyRequest().authenticated()
                );

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
