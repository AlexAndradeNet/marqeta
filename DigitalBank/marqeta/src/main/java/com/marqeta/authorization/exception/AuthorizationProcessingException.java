/* BlankFactor (C)2023 */
package com.marqeta.authorization.exception;

import com.marqeta.authorization.dto.Authorization;
import java.io.Serial;
import java.io.Serializable;

public class AuthorizationProcessingException extends Exception implements Serializable {

    @Serial private static final long serialVersionUID = 1905122041950251207L;
    private final Authorization authorization;

    public AuthorizationProcessingException(String message, Authorization authorization) {
        super(message);
        this.authorization = authorization;
    }

    public Authorization getAuthorization() {
        return this.authorization;
    }
}
