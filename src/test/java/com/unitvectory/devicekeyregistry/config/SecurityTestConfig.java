package com.unitvectory.devicekeyregistry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("test")
public class SecurityTestConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(s -> s.disable())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                // Configure OAuth2 Resource Server
                .oauth2ResourceServer(oauth2 -> oauth2.disable());
        return http.build();
    }
}
