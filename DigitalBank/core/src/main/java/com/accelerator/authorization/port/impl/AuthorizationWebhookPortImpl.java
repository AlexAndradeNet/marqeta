/* BlankFactor (C)2023 */
package com.accelerator.authorization.port.impl;

import com.accelerator.authorization.dto.Authorization;
import com.accelerator.authorization.dto.AuthorizationError;
import com.accelerator.authorization.dto.AuthorizationType;
import com.accelerator.authorization.dto.request.AuthorizationRequest;
import com.accelerator.authorization.exception.InvalidAuthorizationException;
import com.accelerator.authorization.exception.InvalidTransactionException;
import com.accelerator.authorization.mapper.AuthorizationMapper;
import com.accelerator.authorization.port.AuthorizationWebhookPort;
import com.accelerator.authorization.service.AuthorizationClearingService;
import com.accelerator.authorization.service.AuthorizationReversalService;
import com.accelerator.common.exception.DuplicateTransactionException;
import com.accelerator.common.exception.TransitAccountException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationWebhookPortImpl implements AuthorizationWebhookPort {

    private final AuthorizationMapper authorizationMapper;
    private final AuthorizationClearingService authorizationClearingService;
    private final AuthorizationReversalService authorizationReversalService;

    @Override
    public void clearAuthorization(AuthorizationRequest authorizationRequest)
            throws InvalidAuthorizationException,
                    TransitAccountException,
                    DuplicateTransactionException {
        Authorization authorization = authorizationMapper.mapAuthorization(authorizationRequest);
        if (AuthorizationType.AUTHORIZATION_CLEARING == authorization.getType()) {
            validatePrecedingTransactionToken(authorization);
            authorizationClearingService.clearAuthorization(authorization);
        } else {
            throw new InvalidTransactionException(authorization);
        }
    }

    @Override
    public void reverseAuthorization(AuthorizationRequest authorizationRequest)
            throws InvalidAuthorizationException,
                    TransitAccountException,
                    DuplicateTransactionException {
        Authorization authorization = authorizationMapper.mapAuthorization(authorizationRequest);
        if (AuthorizationType.AUTHORIZATION_REVERSAL == authorization.getType()) {
            validatePrecedingTransactionToken(authorization);
            authorizationReversalService.reverseAuthorization(authorization);
        } else {
            throw new InvalidTransactionException(authorization);
        }
    }

    private void validatePrecedingTransactionToken(Authorization authorization)
            throws InvalidAuthorizationException {
        if (StringUtils.isEmpty(authorization.getPrecedingTransactionToken())) {
            throw new InvalidAuthorizationException(
                    AuthorizationError.INVALID_PRECEDING_TRANSACTION_TOKEN.getValue(),
                    authorization);
        }
    }
}
