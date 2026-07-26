/* BlankFactor (C)2023 */
package com.accelerator.authorization.exception;

import com.accelerator.authorization.dto.Authorization;
import com.accelerator.authorization.dto.AuthorizationError;

public class InvalidCardException extends InvalidAuthorizationException {

    public InvalidCardException(Authorization authorization) {
        super(AuthorizationError.INVALID_CARD.getValue(), authorization);
    }
}
