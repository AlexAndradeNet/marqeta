/* BlankFactor (C)2023 */
package com.marqeta.authorization.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.marqeta.authorization.dto.Authorization;
import com.marqeta.authorization.dto.request.AuthorizationRequest;
import com.marqeta.authorization.dto.request.CoreAuthorizationRequest;
import jakarta.validation.ValidationException;

public interface AuthorizationMapper {

    Authorization mapAuthorization(AuthorizationRequest authorizationRequest)
            throws JsonProcessingException, ValidationException;

    CoreAuthorizationRequest mapCoreAuthorizationRequest(Authorization authorization);
}
