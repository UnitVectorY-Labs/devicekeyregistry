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
package com.unitvectory.devicekeyregistry.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.networknt.schema.ValidationMessage;
import com.unitvectory.devicekeyregistry.model.MyErrorResponse;
import com.unitvectory.jsonschema4springboot.ValidateJsonSchemaException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The exception handler test.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class MyExceptionHandlerTest {

    private final MyExceptionHandler handler = new MyExceptionHandler();

    @Test
    public void onValidateJsonSchemaExceptionTest() {
        Set<ValidationMessage> messages = new HashSet<>();

        messages.add(ValidationMessage.builder().message("test1").build());
        messages.add(ValidationMessage.builder().message("test2").build());
        ValidateJsonSchemaException ex = new ValidateJsonSchemaException(messages);

        ResponseEntity<MyErrorResponse> response = handler.onValidateJsonSchemaException(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("JSON validation failed", response.getBody().getError());
        assertEquals(2, response.getBody().getDetails().size());
    }

    @Test
    public void onJsonParseExceptionTest() {
        JsonParseException ex = new JsonParseException("test");

        ResponseEntity<MyErrorResponse> response = handler.onJsonParseException(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Failed to parse JSON", response.getBody().getError());
        assertEquals(1, response.getBody().getDetails().size());
    }

    @Test
    public void onRecordNotFoundExceptionTest() {
        DeviceNotFoundException ex = new DeviceNotFoundException("test");

        ResponseEntity<MyErrorResponse> response = handler.onRecordNotFoundException(ex);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("test", response.getBody().getError());
        assertEquals(0, response.getBody().getDetails().size());
    }

    @Test
    public void onIllegalArgumentExceptionTest() {
        IllegalArgumentException ex = new IllegalArgumentException("test");

        ResponseEntity<MyErrorResponse> response = handler.onJsonParseException(ex);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertEquals(1, response.getBody().getDetails().size());
    }
}
