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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Tests the AudienceClaimValidator class.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class AudienceClaimValidatorTest {

    @Test
    void testValidatorInitialization() {
        AudienceClaimValidator validator = new AudienceClaimValidator("audience");
        assertTrue(validator != null);
    }

    @Test
    void testNullJwt() {
        AudienceClaimValidator validator = new AudienceClaimValidator("audience");
        OAuth2TokenValidatorResult result = validator.validate(null);
        assertTrue(result.hasErrors());
        assertEquals("missing_jwt", result.getErrors().iterator().next().getErrorCode());
    }

    @Test
    void testEmptyRequiredAudience() {
        AudienceClaimValidator validator = new AudienceClaimValidator("");
        Jwt jwt = mock(Jwt.class);
        OAuth2TokenValidatorResult result = validator.validate(jwt);
        assertTrue(result.hasErrors());
        assertEquals("missing_authorized_audience", result.getErrors().iterator().next().getErrorCode());
    }

    @Test
    void testMissingAudienceClaim() {
        AudienceClaimValidator validator = new AudienceClaimValidator("audience");
        Jwt jwt = mock(Jwt.class);
        when(jwt.getAudience()).thenReturn(Collections.emptyList());
        OAuth2TokenValidatorResult result = validator.validate(jwt);
        assertTrue(result.hasErrors());
        assertEquals("invalid_audience", result.getErrors().iterator().next().getErrorCode());
    }

    @Test
    void testInvalidAudience() {
        AudienceClaimValidator validator = new AudienceClaimValidator("audience");
        Jwt jwt = mock(Jwt.class);
        when(jwt.getAudience()).thenReturn(Arrays.asList("wrongAudience"));
        OAuth2TokenValidatorResult result = validator.validate(jwt);
        assertTrue(result.hasErrors());
        assertEquals("invalid_audience", result.getErrors().iterator().next().getErrorCode());
    }

    @Test
    void testValidAudience() {
        AudienceClaimValidator validator = new AudienceClaimValidator("audience");
        Jwt jwt = mock(Jwt.class);
        when(jwt.getAudience()).thenReturn(Arrays.asList("audience"));
        OAuth2TokenValidatorResult result = validator.validate(jwt);
        assertTrue(result.getErrors().isEmpty());
    }
}
