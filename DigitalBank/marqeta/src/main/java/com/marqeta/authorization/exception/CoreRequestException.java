/* BlankFactor (C)2023 */
package com.marqeta.authorization.exception;

import com.marqeta.authorization.dto.response.CoreAuthorizationResponse;
import java.io.Serial;
import java.io.Serializable;

public class CoreRequestException extends Exception implements Serializable {

    @Serial private static final long serialVersionUID = 2905122041950251209L;
    private final CoreAuthorizationResponse response;

    public CoreRequestException(CoreAuthorizationResponse response) {
        super();
        this.response = response;
    }

    public CoreRequestException(String message) {
        super(message);
        this.response = null;
    }

    public CoreAuthorizationResponse getResponse() {
        return this.response;
    }
}
