/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.unitvectory.devicekeyregistry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import com.unitvectory.devicekeyregistry.service.AuthorizationService;

import lombok.AllArgsConstructor;

/**
 * The security configuration
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Profile("!test")
public class SecurityConfig {

        /**
         * THe authorized JWKS URL
         */
        private String authorizedJwks;

        /**
         * THe authorized issuer
         */
        private String authorizedIssuer;

        /**
         * Service used to authorize the request
         */
        private AuthorizationService authorizationService;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                // JWT Decoding and validation
                NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(authorizedJwks).build();
                OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(authorizedIssuer);
                OAuth2TokenValidator<Jwt> withAudience = new AudienceClaimValidator(
                                authorizationService.getAuthorizedAudience());
                OAuth2TokenValidator<Jwt> withSubject = new SubjectClaimValidator(authorizationService);
                OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience,
                                withSubject);
                jwtDecoder.setJwtValidator(validator);

                http.authorizeHttpRequests(
                                authorize -> authorize
                                                // Authenticate /v1/devices
                                                .requestMatchers("/v1/devices").authenticated()
                                                // Authenticate /v1/devices/{deviceId}/status
                                                .requestMatchers("/v1/devices/*/status").authenticated()
                                                // Authenticate /v1/devices/{deviceId}
                                                .requestMatchers("/v1/devices/*").authenticated()
                                                // Authenticate /v1/alias/{deviceAlias}
                                                .requestMatchers("/v1/alias/*").authenticated()
                                                // Authenticate /v1/pending
                                                .requestMatchers("/v1/pending").authenticated()
                                                // Allow all other requests
                                                .anyRequest().permitAll())
                                // Configure OAuth2 Resource Server
                                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder)));
                return http.build();
        }
}
