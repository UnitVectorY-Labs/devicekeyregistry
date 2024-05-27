package com.unitvectory.devicekeyregistry.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.unitvectory.jsonschema4springboot.ValidateJsonSchemaException;
import com.unitvectory.jsonschema4springboot.ValidateJsonSchemaFailedResponse;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(ValidateJsonSchemaException.class)
    public ResponseEntity<ValidateJsonSchemaFailedResponse> onValidateJsonSchemaException(
            ValidateJsonSchemaException ex) {
        return ResponseEntity.badRequest().body(new ValidateJsonSchemaFailedResponse(ex));
    }

    // JsonParseException
}
