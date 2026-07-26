/* BlankFactor (C)2023 */
package com.accelerator.authorization.controller;

import com.accelerator.authorization.dto.request.AuthorizationRequest;
import com.accelerator.authorization.dto.response.AuthorizationResponse;
import com.accelerator.authorization.exception.InvalidAuthorizationException;
import com.accelerator.authorization.port.AuthorizationWebhookPort;
import com.accelerator.common.exception.DuplicateTransactionException;
import com.accelerator.common.exception.TransitAccountException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("port/authorization-webhook")
public class AuthorizationWebhookController {

    private static final String CONTROLLER = "AuthorizationWebhookController";
    private final Logger logger = LoggerFactory.getLogger(AuthorizationWebhookController.class);
    private final AuthorizationWebhookPort authorizationWebhookPort;

    @PostMapping("/clearing")
    public ResponseEntity<AuthorizationResponse> processAuthorization(
            @RequestBody @Valid AuthorizationRequest authorizationRequest)
            throws InvalidAuthorizationException,
                    TransitAccountException,
                    ValidationException,
                    DuplicateTransactionException {

        logger.info("%s: %s".formatted(CONTROLLER, authorizationRequest.toString()));
        authorizationWebhookPort.clearAuthorization(authorizationRequest);

        return ResponseEntity.ok(
                AuthorizationResponse.builder()
                        .token(authorizationRequest.getTransactionToken())
                        .message("OK")
                        .build());
    }

    @PostMapping("/reversal")
    public ResponseEntity<AuthorizationResponse> processAReversalAuthorization(
            @RequestBody @Valid AuthorizationRequest authorizationRequest)
            throws InvalidAuthorizationException,
                    TransitAccountException,
                    ValidationException,
                    DuplicateTransactionException {

        logger.info("%s: %s".formatted(CONTROLLER, authorizationRequest.toString()));
        authorizationWebhookPort.reverseAuthorization(authorizationRequest);

        return ResponseEntity.ok(
                AuthorizationResponse.builder()
                        .token(authorizationRequest.getTransactionToken())
                        .message("OK")
                        .build());
    }
}
