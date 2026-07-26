/* BlankFactor (C)2023 */
package com.accelerator.authorization.exception;

import com.accelerator.authorization.dto.Authorization;
import com.accelerator.authorization.dto.AuthorizationError;

public class InvalidTransactionException extends InvalidAuthorizationException {

    public InvalidTransactionException(Authorization authorization) {
        super(AuthorizationError.TRANSACTION_NOT_PERMITTED.getValue(), authorization);
    }
}
