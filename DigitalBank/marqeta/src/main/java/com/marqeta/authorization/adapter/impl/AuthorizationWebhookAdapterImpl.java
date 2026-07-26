/* BlankFactor (C)2023 */
package com.marqeta.authorization.adapter.impl;

import static com.marqeta.authorization.dto.AuthorizationType.AUTHORIZATION_CLEARING;
import static com.marqeta.authorization.dto.AuthorizationType.AUTHORIZATION_REVERSAL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.marqeta.authorization.adapter.AuthorizationWebhookAdapter;
import com.marqeta.authorization.dto.Authorization;
import com.marqeta.authorization.dto.request.AuthorizationRequest;
import com.marqeta.authorization.exception.AuthorizationProcessingException;
import com.marqeta.authorization.exception.CoreInternalException;
import com.marqeta.authorization.mapper.AuthorizationMapper;
import com.marqeta.authorization.service.CoreAuthWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationWebhookAdapterImpl implements AuthorizationWebhookAdapter {

    private final AuthorizationMapper authorizationMapper;
    private final CoreAuthWebhookService coreAuthWebhookService;

    @Override
    public void processAuthorizationNotification(AuthorizationRequest authorizationRequest)
            throws JsonProcessingException,
                    AuthorizationProcessingException,
                    CoreInternalException {
        Authorization authorization = authorizationMapper.mapAuthorization(authorizationRequest);

        if (AUTHORIZATION_CLEARING.getValue().equals(authorization.getType())) {
            coreAuthWebhookService.processClearing(authorization);
        } else if (AUTHORIZATION_REVERSAL.getValue().equals(authorization.getType())) {
            coreAuthWebhookService.processReversal(authorization);
        }

        // No exception thrown as the webhook is not expecting a response. because we want to ignore
        // unknowns events and response with a 200 code in this case. We need to do this because if
        // the response with an error code, Marqeta will send the same error event 10 times and this
        // could overload the server without any real notification to process
    }
}
