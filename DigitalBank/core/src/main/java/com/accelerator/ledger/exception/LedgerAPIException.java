/* BlankFactor (C)2023 */
package com.accelerator.ledger.exception;

import java.io.Serial;
import java.io.Serializable;

public class LedgerAPIException extends Exception implements Serializable {

    @Serial private static final long serialVersionUID = 2105122041950251207L;

    public LedgerAPIException(String message) {
        super(message);
    }
}
