/* BlankFactor (C)2023 */
package com.blankfactor.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionTypeEnum {
    CLEARING("authorization.clearing"),
    REVERSAL("authorization.reversal");

    private final String value;
}
