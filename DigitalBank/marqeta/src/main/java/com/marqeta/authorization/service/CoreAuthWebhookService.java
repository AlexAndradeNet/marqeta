/* BlankFactor (C)2023 */
package com.marqeta.authorization.service;

import com.marqeta.authorization.dto.Authorization;
import com.marqeta.authorization.exception.AuthorizationProcessingException;
import com.marqeta.authorization.exception.CoreInternalException;

public interface CoreAuthWebhookService {

    void processClearing(Authorization authorization)
            throws AuthorizationProcessingException, CoreInternalException;

    void processReversal(Authorization authorization)
            throws AuthorizationProcessingException, CoreInternalException;
}
