/* BlankFactor (C)2023 */
package com.marqeta.authorization.service.impl;

import com.marqeta.authorization.client.CoreAuthGatewayClient;
import com.marqeta.authorization.dto.Authorization;
import com.marqeta.authorization.dto.request.CoreAuthorizationRequest;
import com.marqeta.authorization.exception.AuthorizationProcessingException;
import com.marqeta.authorization.exception.CoreInternalException;
import com.marqeta.authorization.exception.CoreRequestException;
import com.marqeta.authorization.mapper.AuthorizationMapper;
import com.marqeta.authorization.service.CoreAuthGatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoreAuthGatewayServiceImpl implements CoreAuthGatewayService {

    private final CoreAuthGatewayClient coreAuthGatewayClient;
    private final AuthorizationMapper authorizationMapper;

    @Override
    public void processAuthorization(Authorization authorization)
            throws AuthorizationProcessingException, CoreInternalException {
        CoreAuthorizationRequest request =
                authorizationMapper.mapCoreAuthorizationRequest(authorization);

        try {
            coreAuthGatewayClient.processAuthorization(request);
        } catch (Exception e) {
            if (e.getCause() instanceof CoreRequestException exception) {
                throw new AuthorizationProcessingException(
                        exception.getResponse().getMessage(), authorization);
            } else {
                throw new CoreInternalException(e.getMessage());
            }
        }
    }
}
