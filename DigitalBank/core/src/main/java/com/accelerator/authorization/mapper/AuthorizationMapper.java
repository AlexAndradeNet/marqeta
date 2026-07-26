/* BlankFactor (C)2023 */
package com.accelerator.authorization.mapper;

import com.accelerator.authorization.dto.Authorization;
import com.accelerator.authorization.dto.request.AuthorizationRequest;
import com.accelerator.authorization.exception.InvalidAuthorizationException;

public interface AuthorizationMapper {

    Authorization mapAuthorization(AuthorizationRequest authorizationRequest)
            throws InvalidAuthorizationException;
}
