/* BlankFactor (C)2023 */
package com.accelerator.common.exception;

import lombok.Getter;

@Getter
public class DuplicateTransactionException extends BadRequestException {

    private final String token;

    public DuplicateTransactionException(String message, String token) {
        super(message);
        this.token = token;
    }
}
