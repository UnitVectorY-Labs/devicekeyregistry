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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.networknt.schema.ValidationMessage;
import com.unitvectory.devicekeyregistry.model.MyErrorResponse;
import com.unitvectory.jsonschema4springboot.ValidateJsonSchemaException;

/**
 * The exception handler.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(ValidateJsonSchemaException.class)
    public ResponseEntity<MyErrorResponse> onValidateJsonSchemaException(
            ValidateJsonSchemaException ex) {
        List<String> detailsList = new ArrayList<String>();
        for (ValidationMessage m : ex.getValidationResult()) {
            detailsList.add(m.getMessage());
        }

        return ResponseEntity.badRequest()
                .body(MyErrorResponse.builder().status(400).error("JSON validation failed")
                        .details(Collections.unmodifiableList(detailsList)).build());
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<MyErrorResponse> onJsonParseException(JsonParseException ex) {
        return ResponseEntity.badRequest()
                .body(MyErrorResponse.builder().status(400).error("Failed to parse JSON")
                        .details(Arrays.asList(getStringBeforeLineReturn(ex.getMessage())))
                        .build());
    }

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<MyErrorResponse> onRecordNotFoundException(DeviceNotFoundException ex) {
        return ResponseEntity.status(404).body(MyErrorResponse.builder().status(404)
                .error(ex.getMessage()).details(Arrays.asList()).build());
    }

    /**
     * This is useful in getting only the first part of the error message from an exception so that
     * it can be returned to the user without any additional data that may expose too much
     * information that is not useful.
     * 
     * @param str
     * @return
     */
    private String getStringBeforeLineReturn(String str) {
        if (str == null) {
            return null;
        }

        int index = str.indexOf("\n");
        if (index == -1) {
            return str;
        }
        return str.substring(0, index);
    }
}
