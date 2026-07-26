/* BlankFactor (C)2023 */
package com.accelerator.authorization.controller;

import com.accelerator.authorization.dto.Authorization;
import com.accelerator.authorization.dto.response.AuthorizationResponse;
import com.accelerator.authorization.exception.InvalidAuthorizationException;
import com.accelerator.common.exception.DuplicateTransactionException;
import com.accelerator.common.exception.TransitAccountException;
import com.accelerator.ledger.exception.LedgerAPIException;
import jakarta.validation.ValidationException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AuthorizationCommonController {

    private static final String CONTROLLER_NAME = "AuthorizationCommonController";
    private static final String NULL_TOKEN = "NULL";
    private static final String VALIDATION_ERROR_PREFIX = "Validation error: ";
    private final Logger logger = LoggerFactory.getLogger(AuthorizationCommonController.class);

    @ExceptionHandler({
        InvalidAuthorizationException.class,
        TransitAccountException.class,
        LedgerAPIException.class,
        MethodArgumentNotValidException.class,
        HttpMessageNotReadableException.class,
        ValidationException.class,
        DuplicateTransactionException.class
    })
    @ResponseBody
    public ResponseEntity<AuthorizationResponse> handleException(Exception ex) {
        logger.error("{}: Exception occurred", CONTROLLER_NAME, ex);

        HttpStatus status = determineHttpStatus(ex);
        String message = determineMessage(ex);
        String token = determineToken(ex);

        return new ResponseEntity<>(
                AuthorizationResponse.builder().token(token).message(message).build(), status);
    }

    private HttpStatus determineHttpStatus(Exception ex) {
        if (ex instanceof InvalidAuthorizationException
                || ex instanceof MethodArgumentNotValidException
                || ex instanceof HttpMessageNotReadableException
                || ex instanceof ValidationException
                || ex instanceof DuplicateTransactionException) {
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String determineMessage(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException
                || ex instanceof HttpMessageNotReadableException
                || ex instanceof ValidationException) {
            return VALIDATION_ERROR_PREFIX + ex.getMessage();
        }
        return ex.getMessage();
    }

    private String determineToken(Exception ex) {
        if (ex instanceof InvalidAuthorizationException castedException) {
            return Optional.ofNullable(castedException.getAuthorization())
                    .map(Authorization::getTransactionToken)
                    .orElse(NULL_TOKEN);
        }
        if (ex instanceof DuplicateTransactionException dte) {
            return dte.getToken();
        }
        return NULL_TOKEN;
    }
}
