package com.unitvectory.devicekeyregistry.exception;

import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.unitvectory.devicekeyregistry.model.MyErrorResponse;
import com.unitvectory.jsonschema4springboot.ValidateJsonSchemaException;
import com.unitvectory.jsonschema4springboot.ValidateJsonSchemaFailedResponse;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(ValidateJsonSchemaException.class)
    public ResponseEntity<ValidateJsonSchemaFailedResponse> onValidateJsonSchemaException(
            ValidateJsonSchemaException ex) {
        return ResponseEntity.badRequest().body(new ValidateJsonSchemaFailedResponse(ex));
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<MyErrorResponse> onJsonParseException(JsonParseException ex) {
        return ResponseEntity.badRequest()
                .body(MyErrorResponse.builder().message("Failed to parse JSON")
                        .details(Arrays.asList(getStringBeforeLineReturn(ex.getMessage())))
                        .build());
    }

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<MyErrorResponse> onRecordNotFoundException(DeviceNotFoundException ex) {
        return ResponseEntity.badRequest().body(MyErrorResponse.builder().message(ex.getMessage())
                .details(Arrays.asList()).build());
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
