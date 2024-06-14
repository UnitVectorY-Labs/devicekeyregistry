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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import com.unitvectory.devicekeyregistry.service.AuthorizationService;

/**
 * Tests the SubjectClaimValidator class.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class SubjectClaimValidatorTest {

    @Test
    void testValidatorInitialization() {
        AuthorizationService authorizationService = mock(AuthorizationService.class);
        SubjectClaimValidator validator = new SubjectClaimValidator(authorizationService);
        assertNotNull(validator);
    }

    @Test
    void testValidateWithNullJwt() {
        AuthorizationService authorizationService = mock(AuthorizationService.class);
        SubjectClaimValidator validator = new SubjectClaimValidator(authorizationService);
        OAuth2TokenValidatorResult result = validator.validate(null);
        assertTrue(result.hasErrors());
        assertEquals("missing_jwt", result.getErrors().iterator().next().getErrorCode());
    }

    @Test
    void testValidateWithMissingSubject() {
        AuthorizationService authorizationService = mock(AuthorizationService.class);
        SubjectClaimValidator validator = new SubjectClaimValidator(authorizationService);
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn(null);
        OAuth2TokenValidatorResult result = validator.validate(jwt);
        assertTrue(result.hasErrors());
        assertEquals("invalid_subject", result.getErrors().iterator().next().getErrorCode());
    }

    @Test
    void testValidateWithUnauthorizedSubject() {
        AuthorizationService authorizationService = mock(AuthorizationService.class);
        SubjectClaimValidator validator = new SubjectClaimValidator(authorizationService);
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("unauthorizedSubject");
        when(authorizationService.isSubjectAuthorized("unauthorizedSubject")).thenReturn(false);
        OAuth2TokenValidatorResult result = validator.validate(jwt);
        assertTrue(result.hasErrors());
        assertEquals("invalid_subject", result.getErrors().iterator().next().getErrorCode());
    }

    @Test
    void testValidateWithAuthorizedSubject() {
        AuthorizationService authorizationService = mock(AuthorizationService.class);
        SubjectClaimValidator validator = new SubjectClaimValidator(authorizationService);
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("authorizedSubject");
        when(authorizationService.isSubjectAuthorized("authorizedSubject")).thenReturn(true);
        OAuth2TokenValidatorResult result = validator.validate(jwt);
        assertTrue(result.getErrors().isEmpty());
    }
}
