/* BlankFactor (C)2023 */
package com.accelerator.authorization.exception;

import com.accelerator.authorization.dto.Authorization;
import com.accelerator.common.exception.BadRequestException;

public class InvalidAuthorizationException extends BadRequestException {
    private final transient Authorization authorization;

    public InvalidAuthorizationException(String message, Authorization authorization) {
        super(message);
        this.authorization = authorization;
    }

    public Authorization getAuthorization() {
        return authorization;
    }
}
