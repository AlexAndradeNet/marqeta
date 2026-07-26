/* BlankFactor (C)2023 */
package com.marqeta.authorization.controller;

import com.marqeta.authorization.adapter.AuthorizationWebhookAdapter;
import com.marqeta.authorization.dto.request.AuthorizationNotificationRequest;
import com.marqeta.authorization.dto.request.AuthorizationRequest;
import com.marqeta.authorization.dto.response.AuthorizationErrorResponse;
import com.marqeta.authorization.exception.AuthorizationProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/external/webhook")
public class AuthorizationWebhookController {

    private static final String CONTROLLER = "AuthorizationWebhookController";
    private final Logger logger = LoggerFactory.getLogger(AuthorizationWebhookController.class);
    private final AuthorizationWebhookAdapter authorizationWebhookAdapter;

    @PostMapping
    public ResponseEntity<Object> processNotification(
            @Valid @RequestBody AuthorizationNotificationRequest authorizationNotificationRequest)
            throws Exception {

        logger.info("%s: %s".formatted(CONTROLLER, authorizationNotificationRequest.toString()));
        AuthorizationRequest authorizationRequest =
                authorizationNotificationRequest.getTransactions().get(0);
        authorizationWebhookAdapter.processAuthorizationNotification(authorizationRequest);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(AuthorizationProcessingException.class)
    @ResponseBody
    public ResponseEntity<AuthorizationErrorResponse> handleProcessingError(
            AuthorizationProcessingException ex) {
        logger.error(CONTROLLER + ": " + ex.getMessage(), ex);

        return new ResponseEntity<>(
                AuthorizationErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }
}
