/* BlankFactor (C)2023 */
package com.accelerator.authorization.controller;

import com.accelerator.authorization.dto.request.AuthorizationRequest;
import com.accelerator.authorization.dto.response.AuthorizationResponse;
import com.accelerator.authorization.exception.InvalidAuthorizationException;
import com.accelerator.authorization.port.AuthorizationGatewayPort;
import com.accelerator.common.exception.DuplicateTransactionException;
import com.accelerator.common.exception.TransitAccountException;
import com.accelerator.ledger.exception.LedgerAPIException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/port/authorization-gateway")
public class AuthorizationGatewayController {

    private static final String CONTROLLER = "AuthorizationGatewayController";
    private final Logger logger = LoggerFactory.getLogger(AuthorizationGatewayController.class);
    private final AuthorizationGatewayPort authorizationGatewayPort;

    @PostMapping
    public ResponseEntity<AuthorizationResponse> processAuthorization(
            @RequestBody @Valid AuthorizationRequest authorizationRequest)
            throws InvalidAuthorizationException,
                    TransitAccountException,
                    LedgerAPIException,
                    DuplicateTransactionException {

        logger.info("%s: %s".formatted(CONTROLLER, authorizationRequest.toString()));
        authorizationGatewayPort.processAuthorization(authorizationRequest);

        return ResponseEntity.ok(
                AuthorizationResponse.builder()
                        .token(authorizationRequest.getTransactionToken())
                        .message("OK")
                        .build());
    }
}
