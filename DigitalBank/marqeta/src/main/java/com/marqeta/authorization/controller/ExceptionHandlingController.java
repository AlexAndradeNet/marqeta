/* BlankFactor (C)2023 */
package com.marqeta.authorization.controller;

import com.marqeta.authorization.dto.response.AuthorizationErrorResponse;
import com.marqeta.authorization.exception.CoreInternalException;
import jakarta.validation.ValidationException;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    public ResponseEntity<AuthorizationErrorResponse> handleValidationException(Exception ex) {
        String errorMessage;

        if (ex instanceof MethodArgumentNotValidException castedEx) {
            errorMessage =
                    Objects.requireNonNull(castedEx.getBindingResult().getFieldError())
                            .getDefaultMessage();
        } else {
            errorMessage = ex.getMessage();
        }

        return ResponseEntity.badRequest()
                .body(
                        AuthorizationErrorResponse.builder()
                                .message("Validation error: " + errorMessage)
                                .status(HttpStatus.BAD_REQUEST.value())
                                .build());
    }

    @ExceptionHandler(CoreInternalException.class)
    @ResponseBody
    public ResponseEntity<AuthorizationErrorResponse> handleInternalError(
            CoreInternalException ex) {

        return new ResponseEntity<>(
                AuthorizationErrorResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
