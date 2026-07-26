/* BlankFactor (C)2023 */
package com.accelerator.authorization.port.impl;

import com.accelerator.authorization.dto.Authorization;
import com.accelerator.authorization.dto.AuthorizationType;
import com.accelerator.authorization.dto.request.AuthorizationRequest;
import com.accelerator.authorization.exception.InvalidAuthorizationException;
import com.accelerator.authorization.exception.InvalidTransactionException;
import com.accelerator.authorization.mapper.AuthorizationMapper;
import com.accelerator.authorization.port.AuthorizationGatewayPort;
import com.accelerator.authorization.service.AuthorizationProcessingService;
import com.accelerator.common.exception.DuplicateTransactionException;
import com.accelerator.common.exception.TransitAccountException;
import com.accelerator.ledger.exception.LedgerAPIException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationGatewayPortImpl implements AuthorizationGatewayPort {

    private final AuthorizationMapper authorizationMapper;
    private final AuthorizationProcessingService authorizationProcessingService;

    @Override
    public void processAuthorization(AuthorizationRequest authorizationRequest)
            throws InvalidAuthorizationException,
                    TransitAccountException,
                    LedgerAPIException,
                    DuplicateTransactionException {
        Authorization authorization = authorizationMapper.mapAuthorization(authorizationRequest);
        if (AuthorizationType.AUTHORIZATION == authorization.getType()) {
            authorizationProcessingService.processAuthorization(authorization);
        } else {
            throw new InvalidTransactionException(authorization);
        }
    }
}
