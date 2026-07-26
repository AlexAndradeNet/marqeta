/* BlankFactor (C)2023 */
package com.accelerator.common.exception;

import java.io.Serial;
import java.io.Serializable;

public class BadRequestException extends Exception implements Serializable {
    @Serial private static final long serialVersionUID = 1905122041950251207L;

    public BadRequestException(String message) {
        super(message);
    }
}
