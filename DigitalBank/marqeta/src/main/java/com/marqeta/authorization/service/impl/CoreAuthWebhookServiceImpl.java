/* BlankFactor (C)2023 */
package com.marqeta.authorization.service.impl;

import com.marqeta.authorization.client.CoreAuthWebhookClient;
import com.marqeta.authorization.dto.Authorization;
import com.marqeta.authorization.dto.request.CoreAuthorizationRequest;
import com.marqeta.authorization.exception.AuthorizationProcessingException;
import com.marqeta.authorization.exception.CoreInternalException;
import com.marqeta.authorization.exception.CoreRequestException;
import com.marqeta.authorization.mapper.AuthorizationMapper;
import com.marqeta.authorization.service.CoreAuthWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoreAuthWebhookServiceImpl implements CoreAuthWebhookService {

    private final CoreAuthWebhookClient coreAuthWebhookClient;
    private final AuthorizationMapper authorizationMapper;

    @Override
    public void processClearing(Authorization authorization)
            throws AuthorizationProcessingException, CoreInternalException {
        CoreAuthorizationRequest request =
                authorizationMapper.mapCoreAuthorizationRequest(authorization);

        try {
            coreAuthWebhookClient.processClearing(request);
        } catch (Exception e) {
            if (e.getCause() instanceof CoreRequestException exception) {
                throw new AuthorizationProcessingException(
                        exception.getResponse().getMessage(), authorization);
            } else {
                throw new CoreInternalException(e.getMessage());
            }
        }
    }

    @Override
    public void processReversal(Authorization authorization)
            throws AuthorizationProcessingException, CoreInternalException {
        CoreAuthorizationRequest request =
                authorizationMapper.mapCoreAuthorizationRequest(authorization);

        try {
            coreAuthWebhookClient.processReversal(request);
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
