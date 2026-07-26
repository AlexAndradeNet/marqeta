/* BlankFactor (C)2023 */
package com.marqeta.authorization.controller;

import com.marqeta.authorization.adapter.AuthorizationGatewayAdapter;
import com.marqeta.authorization.dto.Authorization;
import com.marqeta.authorization.dto.request.AuthorizationRequest;
import com.marqeta.authorization.dto.response.AuthorizationResponse;
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
@RequestMapping("/external/gateway/jit")
public class AuthorizationGatewayController {

    private static final String CONTROLLER = "AuthorizationGatewayController";
    private final Logger logger = LoggerFactory.getLogger(AuthorizationGatewayController.class);
    private final AuthorizationGatewayAdapter authorizationGatewayAdapter;

    @PostMapping
    public ResponseEntity<Object> createAuthorization(
            @RequestBody @Valid AuthorizationRequest authorizationRequest) throws Exception {

        logger.info("%s: %s".formatted(CONTROLLER, authorizationRequest.toString()));

        Authorization authorizationResult =
                authorizationGatewayAdapter.processAuthorization(authorizationRequest);
        return ResponseEntity.ok(createAuthorizationResponse(authorizationResult));
    }

    public AuthorizationResponse createAuthorizationResponse(Authorization authorization) {
        AuthorizationResponse.JitFunding jitFunding =
                AuthorizationResponse.JitFunding.builder()
                        .token(authorization.getJitFundingToken())
                        .method(authorization.getMethod())
                        .userToken(authorization.getUserToken())
                        .actingUserToken(authorization.getActingUserToken())
                        .amount(authorization.getAmount())
                        .memo(authorization.getMemo())
                        .tags(authorization.getTags())
                        .build();
        return AuthorizationResponse.builder().jitFunding(jitFunding).build();
    }

    @ExceptionHandler(AuthorizationProcessingException.class)
    @ResponseBody
    public ResponseEntity<AuthorizationResponse> handleAuthorizationProcessingError(
            AuthorizationProcessingException ex) {
        AuthorizationResponse authorizationResponse =
                createAuthorizationErrorResponse(ex.getAuthorization(), ex.getMessage());
        logger.error(CONTROLLER + ": " + ex.getMessage(), ex);
        return new ResponseEntity<>(authorizationResponse, HttpStatus.BAD_REQUEST);
    }

    private AuthorizationResponse createAuthorizationErrorResponse(
            Authorization authorization, String message) {
        AuthorizationResponse.JitFunding jitFunding =
                AuthorizationResponse.JitFunding.builder()
                        .token(authorization.getJitFundingToken())
                        .method(authorization.getMethod())
                        .userToken(authorization.getUserToken())
                        .actingUserToken(authorization.getActingUserToken())
                        .amount(authorization.getAmount())
                        .memo(authorization.getMemo())
                        .tags(authorization.getTags())
                        .declineReason(message)
                        .build();
        return AuthorizationResponse.builder().jitFunding(jitFunding).build();
    }
}
