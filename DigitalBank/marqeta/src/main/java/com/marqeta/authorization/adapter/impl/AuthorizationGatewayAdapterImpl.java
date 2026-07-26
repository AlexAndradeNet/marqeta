/* BlankFactor (C)2023 */
package com.marqeta.authorization.adapter.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.marqeta.authorization.adapter.AuthorizationGatewayAdapter;
import com.marqeta.authorization.dto.Authorization;
import com.marqeta.authorization.dto.request.AuthorizationRequest;
import com.marqeta.authorization.exception.AuthorizationProcessingException;
import com.marqeta.authorization.exception.CoreInternalException;
import com.marqeta.authorization.mapper.AuthorizationMapper;
import com.marqeta.authorization.service.CoreAuthGatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationGatewayAdapterImpl implements AuthorizationGatewayAdapter {

    private final CoreAuthGatewayService coreAuthGatewayService;
    private final AuthorizationMapper authorizationMapper;

    @Override
    public Authorization processAuthorization(AuthorizationRequest authorizationRequest)
            throws JsonProcessingException,
                    AuthorizationProcessingException,
                    CoreInternalException {
        Authorization authorization = authorizationMapper.mapAuthorization(authorizationRequest);
        coreAuthGatewayService.processAuthorization(authorization);

        return authorization;
    }
}
