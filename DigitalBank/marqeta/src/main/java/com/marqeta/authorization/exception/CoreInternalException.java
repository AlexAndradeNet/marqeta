/* BlankFactor (C)2023 */
package com.marqeta.authorization.exception;

import java.io.Serial;
import java.io.Serializable;

public class CoreInternalException extends Exception implements Serializable {

    @Serial private static final long serialVersionUID = 2905122041950251509L;

    public CoreInternalException(String message) {
        super(message);
    }
}
