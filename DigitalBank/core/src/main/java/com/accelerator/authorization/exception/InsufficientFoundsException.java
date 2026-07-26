/* BlankFactor (C)2023 */
package com.accelerator.authorization.exception;

import com.accelerator.authorization.dto.Authorization;
import com.accelerator.authorization.dto.AuthorizationError;

public class InsufficientFoundsException extends InvalidAuthorizationException {

    public InsufficientFoundsException(Authorization authorization) {
        super(AuthorizationError.INSUFFICIENT_FUNDS.getValue(), authorization);
    }
}
