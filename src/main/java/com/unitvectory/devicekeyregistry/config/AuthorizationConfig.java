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

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.unitvectory.devicekeyregistry.service.AuthorizationService;
import com.unitvectory.devicekeyregistry.service.AuthorizationServiceImpl;

/**
 * The authorization configuration
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@Profile("!test")
@Configuration
public class AuthorizationConfig {

    /**
     * URL for validating their JWT signature
     */
    @Value("${devicekeyregistry.authorized.jwks:https://www.googleapis.com/oauth2/v3/certs}")
    private String jwks;

    /**
     * Issuer claim must match
     */
    @Value("${devicekeyregistry.authorized.issuer:https://accounts.google.com}")
    private String issuer;

    /**
     * The audience claim must match
     */
    @Value("${devicekeyregistry.authorized.audience}")
    private String authorizedAudience;

    /**
     * The subject claim must be one of
     */
    @Value("#{'${devicekeyregistry.authorized.subjects}'.split(',')}")
    private List<String> authorizedSubjects;

    @Bean
    public AuthorizationService authorizationService() {
        return new AuthorizationServiceImpl(authorizedAudience, authorizedSubjects);
    }

    @Bean
    public String authorizedJwks() {
        return this.jwks;
    }

    @Bean
    public String authorizedIssuer(){
        return this.issuer;
    }
}
