/* BlankFactor (C)2023 */
package com.marqeta.authorization.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.marqeta.authorization.dto.request.AuthorizationRequest;
import com.marqeta.authorization.exception.AuthorizationProcessingException;
import com.marqeta.authorization.exception.CoreInternalException;

public interface AuthorizationWebhookAdapter {

    void processAuthorizationNotification(AuthorizationRequest authorizationRequest)
            throws JsonProcessingException, AuthorizationProcessingException, CoreInternalException;
}
