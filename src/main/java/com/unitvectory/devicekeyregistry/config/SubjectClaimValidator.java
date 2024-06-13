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

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import com.unitvectory.devicekeyregistry.service.AuthorizationService;

import lombok.AllArgsConstructor;

/**
 * Custom logic to validate the sub claim in the JWT
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@AllArgsConstructor
class SubjectClaimValidator implements OAuth2TokenValidator<Jwt> {

    /**
     * The service used to validate the if a subject is authorized
     */
    private final AuthorizationService authorizationService;

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {

        // Check that the JWT was provided
        if (jwt == null) {
            return OAuth2TokenValidatorResult.failure(new OAuth2Error("missing_jwt",
                    "The required JWT is missing or invalid", null));
        }

        // Check that the
        if (authorizationService == null) {
            return OAuth2TokenValidatorResult.failure(new OAuth2Error("missing_authorization_service",
                    "The required authorization service is missing", null));

        }

        // Check if the subject claim is present and is authorized
        String subject = jwt.getSubject();
        if (subject == null || subject.isBlank() || !this.authorizationService.isSubjectAuthorized(subject)) {
            OAuth2Error error = new OAuth2Error("invalid_subject",
                    "The required subject is missing or invalid", null);
            return OAuth2TokenValidatorResult.failure(error);
        }

        // If the audience claim is present and valid, return a success result.
        return OAuth2TokenValidatorResult.success();
    }
}